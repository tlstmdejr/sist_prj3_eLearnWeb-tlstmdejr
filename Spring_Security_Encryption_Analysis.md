# Spring Security 암호화 방식 비교 및 최적화 분석

## 1. 암호화 방식 비교: Random IV vs Fixed IV

두 시스템에서 사용 중인 암호화 방식은 **"초기화 벡터(IV)"**를 어떻게 처리하느냐에 따라 검색 가능 여부와 보안성이 달라집니다.

```mermaid
graph TD
    Data[사용자 입력: '홍길동'] --> Enc1{MemberService 방식<br>(Random IV)}
    Data --> Enc2{UserService 방식(Fixed IV)}
    
    Enc1 --> |1회차 암호화| Res1[결과: a8f9...]
    Enc1 --> |2회차 암호화| Res2[결과: b2c3...]
    Res1 -.-> |다름| Res2
    
    Enc2 --> |1회차 암호화| Res3[결과: k9x1...]
    Enc2 --> |2회차 암호화| Res4[결과: k9x1...]
    Res3 === |같음| Res4
```
ㅈ
### ① MemberService (표준/권장 방식)
- **파일**: `spring_prj/.../MemberService.java`
- **방식**: `Encryptors.text(key, salt)` 사용
- **특징**: 매번 새로운 **Random IV(Salt)**가 생성되어 암호문에 포함됨.
- **장점**: 같은 단어를 암호화해도 결과가 매번 달라져 보안성이 매우 뛰어남 (패턴 분석 불가).
- **단점**: 암호문이 매번 바뀌므로, DB에서 `WHERE name = '암호화된값'`과 같은 **검색이 불가능**.

### ② UserService (현재 방식)
- **파일**: `sist_prj3_eLearnWeb-tlstmdejr/.../UserService.java`
- **방식**: `createEncryptor()` 메서드를 통해 **고정 IV(0으로 채운 배열)** 사용
- **특징**: 언제 암호화하든 **항상 동일한 암호문**이 생성됨.
- **장점**: 암호문이 고정되어 있으므로, DB 비교 검색(중복 확인 등)이 가능.
- **단점**: 동일한 평문은 동일한 암호문이 되므로, 데이터 분포 패턴이 노출될 수 있어 보안성이 상대적으로 낮음.

#### 🔍 코드 상세 분석 (주석 포함)

```java
// 1. TextEncryptor 인터페이스를 익명 클래스로 직접 구현합니다.
// - 목적: 스프링 시큐리티의 기본 Encryptors는 Random IV를 강제하므로, 
//         이를 우회하여 Fixed IV(고정된 초기값)를 쓰기 위함입니다.
private TextEncryptor createEncryptor() {
    return new TextEncryptor() {
        
        // 2. 내부적으로 AES 암호화 객체를 생성합니다.
        // - key, salt: 암호화에 쓸 키와 솔트
        // - BytesKeyGenerator: 여기서 핵심! 매번 새로운 IV를 만드는 대신, 
        //   항상 0으로 채워진 16바이트 배열을 반환하도록 재정의(Override)했습니다.
        private final AesBytesEncryptor encryptor = new AesBytesEncryptor(key, salt, new BytesKeyGenerator() {
            @Override
            public int getKeyLength() { return 16; } // AES 블록 크기 (16 bytes)

            @Override
            public byte[] generateKey() {
                return new byte[16]; // ★ 항상 똑같은 값(000...0)을 반환 -> 암호문이 고정됨
            }
        });

        // 3. 암호화 메서드 (Encrypt)
        @Override
        public String encrypt(String text) {
            // (1) 문자열을 바이트로 변환
            // (2) AES 암호화 수행 (고정 IV 사용)
            // (3) 결과를 Hex(16진수 문자열)로 인코딩하여 반환
            return new String(Hex.encode(encryptor.encrypt(text.getBytes(StandardCharsets.UTF_8))));
        }

        // 4. 복호화 메서드 (Decrypt)
        @Override
        public String decrypt(String encryptedText) {
            // (1) Hex 문자열을 다시 바이트로 디코딩
            // (2) AES 복호화 수행
            // (3) 결과를 문자열로 변환하여 반환
            return new String(encryptor.decrypt(Hex.decode(encryptedText)), StandardCharsets.UTF_8);
        }
    };
}
```


---

## 2. 코드 최적화 제안 (리팩토링)

현재 `UserService.java`의 `createEncryptor()` 메서드는 메서드를 호출할 때마다 새로운 `TextEncryptor` 객체와 내부의 `AesBytesEncryptor` 객체를 생성합니다. 이는 불필요한 메모리 할당과 GC 부하를 발생시킬 수 있습니다.

**※ 주의**: 아래 방법들은 **암호화 결과(Ciphertext)는 현재 방식과 100% 동일하게 유지**하면서, 내부 구조만 개선하는 방법입니다.

### 방안 A: @PostConstruct를 이용한 Bean 초기화 (추천)
Service가 생성될 때 암호화 객체를 딱 한 번만 만들어두고 재사용하는 방법입니다.

```java
@Service
public class UserService {
    // 1. 멤버 변수로 선언
    private TextEncryptor encryptor;

    @Value("${user.crypto.key}")
    private String key;
    @Value("${user.crypto.salt}")
    private String salt;

    // 2. 초기화 메서드 (@PostConstruct)
    @PostConstruct
    public void init() {
        // 객체를 한 번만 생성하여 메모리 절약
        this.encryptor = new TextEncryptor() {
            private final AesBytesEncryptor delegate = new AesBytesEncryptor(key, salt, () -> new byte[16]); // 고정 IV
            
            @Override
            public String encrypt(String text) {
                return new String(Hex.encode(delegate.encrypt(text.getBytes(StandardCharsets.UTF_8))));
            }
            @Override
            public String decrypt(String encryptedText) {
                return new String(delegate.decrypt(Hex.decode(encryptedText)), StandardCharsets.UTF_8);
            }
        };
    }

    // 3. 사용 (메서드 호출 불필요)
    public boolean addUser(UserDTO sDTO) {
        // 기존: sDTO.setName(createEncryptor().encrypt(sDTO.getName()));
        // 변경:
        sDTO.setName(this.encryptor.encrypt(sDTO.getName())); 
    }
}
```

### 방안 B: 별도 유틸리티 컴포넌트로 분리
암호화 로직을 아예 다른 클래스로 분리하여, 다른 Service에서도 재사용할 수 있게 만드는 방법입니다.

**1. DeterministicCrypto.java 생성**
```java
@Component
public class DeterministicCrypto implements TextEncryptor {
    
    private final AesBytesEncryptor delegate;

    public DeterministicCrypto(@Value("${user.crypto.key}") String key, 
                               @Value("${user.crypto.salt}") String salt) {
        // 생성자에서 초기화
        this.delegate = new AesBytesEncryptor(key, salt, () -> new byte[16]);
    }

    @Override
    public String encrypt(String text) {
        return new String(Hex.encode(delegate.encrypt(text.getBytes(StandardCharsets.UTF_8))));
    }

    @Override
    public String decrypt(String encryptedText) {
        return new String(delegate.decrypt(Hex.decode(encryptedText)), StandardCharsets.UTF_8);
    }
}
```

**2. UserService.java에서 사용**
```java
@Service
public class UserService {
    
    @Autowired
    private DeterministicCrypto crypto; // 주입받음

    public boolean addUser(UserDTO sDTO) {
        sDTO.setName(crypto.encrypt(sDTO.getName())); // 사용
    }
}
```

---

## 3. 결론

1.  **현상 유지**: 현재 방식(`createEncryptor` 매번 호출)도 기능상 문제는 없으나, 요청이 많아지면 성능에 약간의 영향이 있을 수 있습니다.
2.  **보안 강화**: 근본적으로는 **Random IV 방식(MemberService)**을 따르고, 검색이 필요한 컬럼(이름)에 대해서만 **Blind Index(별도 해시 컬럼)**를 추가하는 것이 보안상 가장 이상적입니다.
3.  **최적화**: DB 변경 없이 구조만 개선하고 싶다면 **방안 A (@PostConstruct)**를 적용하는 것이 가장 간단하고 효과적입니다.
