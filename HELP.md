
````mermaid
sequenceDiagram
 participant C as Client
 participant AC as AuthController
 participant UD as UserService
 participant R as Repository
 participant D as Database
 participant AM as AuthManager
 participant J as JwtService
 C->>+AC: Login Request (username, password)
 AC->>+UD: loadUserByUsername(username)
 UD->>+R: findByUserName(username)
 R->>+D: gets user by username
 D->>-R: user returned
 R->>-UD: UserDetails including roles
 UD->>-AC: UserDetails
 alt Successful Authentication
  AC->>+AM: authManager.authenticate(UsernamePasswordAuthenticationToken)
  Note right of AM : validates password 
  AM->>-AC: Authentication (when successful)
  AC->>+J: generateToken(UserDetails)
  J->>-AC: JWT Token
  AC-->>-C: JWT Token
 else Authentication Failed
  AC->>+AM: authManager.authenticate(UsernamePasswordAuthenticationToken)
  Note right of AM : validates password 
  AM->>+AC: throws an AuthenticationException(ex)
  AC-->>-C: forbidden
 end


````

````mermaid
classDiagram
    namespace SPRING {
        class SecurityConfig {
            +configure(HttpSecurity http)
        }

        class JwtRequestFilter {
            +doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        }

    }
   

    namespace securityExecution {
      
        class JwtService {
            +generateToken(UserDetails userDetails)
            +validateToken(String token)
        }
    }

    namespace ApiExecution {
        class SecureController {
            +securedEndpoint()
        }
        class PublicController {
            +publicEndpoint()
        }
        class UserService {
            +loadUserByUsername(String username)
        }
        
        class User {
            -String username
            -String password
            -List<Role> roles
        }
        class Role {
            -String name
        }
    }

    namespace GetToken {

        class AuthController {
            +authenticateUser(UserLoginRequestDTO loginRequest)
        }
        class UserLoginRequestDTO {
            -String username
            -String password
        }
    }

    AuthController --|> UserLoginRequestDTO : uses
    AuthController --|> JwtService : uses
    AuthController --|> UserService : authenticates through
    UserService --|> User : loads
    User --|> Role : has
    SecurityConfig --|> JwtRequestFilter : configures
    JwtRequestFilter --|> JwtService : uses
    JwtService --|> UserService : validates user details with
    SecureController ..> JwtRequestFilter : requires token validation
    PublicController ..> JwtRequestFilter : optional token validation


````

````mermaid
sequenceDiagram
    participant C as Client
    participant JF as JwtRequestFilter
    participant SC as SecureController
    participant J as JwtService
    participant S as Service
    participant R as Repository
    participant D as Database

    C->>+JF: Request to Secure Endpoint with JWT
    JF->>+J: validateToken(JWT)
    J->>-JF: Validation Result
    alt Valid Token
        JF->>+SC: Forward Request
        SC ->>+ S: getResource(filter data)
        S ->>+R: findBy....(filter data)
        R ->>+ D: gets data
        D ->>- R: returns resource
        R ->>- S: returns resource
        S ->>- SC: return resource
        SC-->>-C: returns resourceDTO
    else Invalid Token
        JF-->>-C: Access Denied
    end

````