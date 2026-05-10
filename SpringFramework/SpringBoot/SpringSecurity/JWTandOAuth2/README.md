



### Encryption and Decryption

- If person A sends a data over the internet to person B
	- the data should not be visible to others
	- even if it is visible, the data must not be read
	- they should not be able to make changes to this data
	- even if something was changed `B` should be able to identify it
- We can do this with the help of `Cryptography`
	- in `Cryptography` there is a concept of `Encryption` and `Decryption`

- Encryption -> converting normal text to cipher text
- with the help of key
![](assets/2026-04-21-21-31-56.png)

- Decryption -> converting cipher text to normal text
- decrypt with the help of same key used for Encryption
![](assets/2026-04-21-21-32-15.png)

- Key can be of two types
1. `Symmetric key`
	- same key will be used for Encryption and Decryption
	- this is called Symmetric key Cryptography
	- Problem is that this key needs to be shared before sending the actual data
	- this is faster
	- we can have large key size
		- bigger the key size more secure
	- If there are multiple members in the network then there needs to be different key
	- If `A` have to communicate with all the members the `A` have to manage all the different keys
	- The solution for this is `Asymmetric` Cryptography
2. `Asymmetric key`
	- It uses a concept of `public` and `private` key
	- For encryption and decryption we will be using two keys public and private keys
		- If we `encrypt` the data with `private` key then we have to `decrypt` the data with help of `public` key

![](assets/2026-04-21-21-41-27.png)
		- If we `encrypt` the data with `public` key we have to `decrypt` the data with `private` key

- the `private` key is secure and not shared to anyone
- For example
	- `A` will encrypt the message with `B`'s `public` key
	- `B` will use it's `private` key to decrypt the message

- There multiple algorithms available 
- Symmetric key
	- AES (Advanced Encryption Standard)
	- DES (Data Encryption Standard)

- Asymmetric key
	- RSA (Rivest, Shamir, Adleman)
	- ECC (Elliptic Curve Cryptography)

- With Asymmetric key Cryptography, there is no way to prove that the sender is the actual sender
- for this purpose we use `Digital Signature`

---

### Digital Signature

- We can prove that `A` has send the message, If
	- `A` encrypts the message with it's own `private` key
	- `B` will decrypt the message using `A`'s public key
	- if the decryption is possible they we can say that `A` has sent the message
	- Now, lets say `C` hacks the message
		- creates a new message with it's own `private` key
		- and sends it to `B`
		- When `B` tries to decrypt with `A`'s public key, it won't work
		- here we can tell that `B` has not sent the message

- So, here we do not have security, we have a proof who sent the data
	- anyone can read the data
	- When `A` sends the message, `C`(mediator) is able to decrypt it using `A's` `public` key as `A` has used it's private key and change the data
- To secure the data, we need to do a `double` encryption
	- First, `A` will encrypt the message with `B's` `public` key.
		- then again `A` will encrypt using `A's` `private` key
	- `B` will decrypt it first, with `A's` `public` key
		- data will still be encrypted
		- then `B` will decrypt with it's own `private` key

- Now if `C` hacks and receives the package
	- The package is encrypted with `A's` `private` key, it will decrypt with `A's` `public` key
	- Now the message is still encrypted with `B's` public
		- it can't decrypt it as it does not have `B's` private key

---

### JWT (JSON web token) 

- Client asks the server for resource
	- if it is a static page, we can simply return it
- What if we want to change the data based on user request
- In this case the server should know who is sending the request
	- We can use `login`
	- But after login server is basically `Stateless`
- One way is that we can make an entry on the `server` side
	- The server will send a `Session ID`, which will then be stored in client side `cookie`
	- Every time client request we can use this id, and no need to login every time
	- We have used `JSESSIONID` before
- The problem comes when we try to scale
	- When we have `multiple` servers, when we scaled horizontally
	- with this `JSESSIONID` will not work as the `Session ID` will be with only one server
	- We can use two ways
		- We can have a shared database which stores the `Session ID`, every time we get a request check from the shared database
		- Or we can tell the `load balancer`, whenever a request from this `user` send it to this particular server.
- What if the server can give a `Pass` (coffee shop example), now everytime a particular request is sent we can show this pass 
	- We have to also `sign` the pass
- To implement this we have `JWT`

---

### What is JWT

- `JSON` web tokens are open, industry standard RFC 7519 method of representing claims securely between two parties.
- When we send token from server to client initially
	- it can be in two formats
		1. JSON
		2. XML
	- In xml the length of the data will be lengthy, for small amount of data there will be multiple tags 
	- we can use normal JSON, sometimes JSON can be lengthy as well
	- We have to use some encoded format
		- this is where JSON web tokens comes into picture
- Visualize JWT - [jwt.io](https://jwt.io)
- There are different kinds of algorithms
	- HS256 
		- HMAC SHA-256 -> Symmetric
	- RS256
		- Asymmetric
	- higher the number, bigger the key more secure


- JWT is encoded 
	- the encoded part consists of three sections
	- Decoded parts
1. `Decoded Header`
```json
{
"alg": "HS256",
"typ": "JWT"
}
```

![](assets/2026-04-23-07-54-18.png)

2. `Decoded Paylod`
	- this is where we send the data
	- information, stays inside payload
```json
{
	"sub": "1234567890",
	"name": "John Doe",
	"admin": true,
	"iat": 1516239022
}
```
- we can also send `exp:datetime`

![](assets/2026-04-23-07-54-33.png)

3. `Verify Signature`
- When we say we want to sign the tokens, we have to use this signature
- When we are using RSA, we will get two different keys `private` and `public`
- When using `HS256`, we get only one key which will be shared by both server an client

![](assets/2026-04-23-07-58-28.png)

- we can encrypt it
- while encryption is not actual encryption
	- basically telling that no one should be able to modify the tokens

- We have an option to encrypt the jwt tokens itself
- If we are not encrypting it then anyone can see the data which we are sending
- so we should not put secret data in the payload

---

### Custom Login 

- We use the same project used for spring-security
- we allow the `register` and `login` request 
	- they don't have to be authenticate
```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

	return http.csrf(Customizer -> Customizer.disable())
	.authorizeHttpRequests(
		request -> request.requestMatchers("register", "login").permitAll().anyRequest().authenticated())
	.httpBasic(Customizer.withDefaults())
	.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	.build();
}
```

- We need to check if the `Username` and `Password` is correct for generating the `token`
- Inbuilt in spring-security we have `UserPasswordAuthenticationToken`
	- Everytime it verifies the username and password it will generate the token
	- with that we can check if the user is valid or not
	- `UserPasswordAuthenticationToken` returns `Authentication` object
		- We have to put the data of `User` into `Authentication`
		- We can do that through `AuthenticationManager` object
	- Create an `AuthenticationManager` bean in `SecurityConfig` file and inject it in `Controller`
```java

@Autowired
private AuthenticationManager authenticationManager;

@PostMapping("/login")
public String login(@RequestBody User user) {
	try {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				user.getUsername(), user.getPassword()));

		return "Success";

	} catch (AuthenticationException e) {
		return "Login failed";
	}
}
```

```java
@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	return config.getAuthenticationManager();
}
```

- `AuthenticationManager` -> we can pass `AuthenticationConfiguration config` object
	- using this we get an object of type `AuthenticationManager`

- While trying with `wrong credentials` for checking `Login failed`, we get `401 Unauthorized` if we use an `if` condition
- As Spring security's internal filter chain throws `BadCredentialsException` and the code never reaches the else block

- Now, the task is to generate and return the `JWT` token once the user is authenticated.

---

### Generating Token

- Create a new service class `JwtService`

```java
@Autowired
private JwtService jwtService;

@PostMapping("/login")
public String login(@RequestBody User user) {
	try {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				user.getUsername(), user.getPassword()));

		return jwtService.generateToken(user.getUsername());

	} catch (AuthenticationException e) {
		return "Login failed";
	}
}


// JwtService class
@Service
public class JwtService {
	public String generateToken(String username) {

	}
}
```

- Add dependency for `JWT` in `pom.xml`
```xml
<dependency>
	<groupId>io.jsonwebtoken</groupId>
	<artifactId>jjwt-api</artifactId>
	<version>0.13.0</version>
	<scope>compile</scope>
</dependency>

<dependency>
	<groupId>io.jsonwebtoken</groupId>
	<artifactId>jjwt-impl</artifactId>
	<version>0.13.0</version>
	<scope>runtime</scope>
</dependency>

<dependency>
	<groupId>io.jsonwebtoken</groupId>
	<artifactId>jjwt-jackson</artifactId>
	<version>0.13.0</version>
	<scope>runtime</scope>
</dependency>
```

> [!NOTE]
> If getting `Exception` then only use `jjwt-jackson` for json conversion
```logs
2026-04-24T22:43:59.961+05:30  INFO 78223 --- [spring-security-demo] [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 1 ms
$2a$12$IrhPP5eznLakA4q56MZV4uCqUZr0kUBwPlZHUE5gyWddfUFKqkRF2
Hibernate: insert into users (password,username) values (?,?)
Hibernate: select u1_0.id,u1_0.password,u1_0.username from users u1_0 where u1_0.username=?
2026-04-24T22:44:11.262+05:30 ERROR 78223 --- [spring-security-demo] [nio-8080-exec-3] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed: io.jsonwebtoken.impl.lang.UnavailableImplementationException: Unable to find an implementation for interface io.jsonwebtoken.io.Serializer using java.util.ServiceLoader. Ensure you include a backing implementation .jar in the classpath, for example jjwt-jackson.jar, jjwt-gson.jar or jjwt-orgjson.jar, or your own .jar for custom implementations.] with root cause

io.jsonwebtoken.impl.lang.UnavailableImplementationException: Unable to find an implementation for interface io.jsonwebtoken.io.Serializer using java.util.ServiceLoader. Ensure you include a backing implementation .jar in the classpath, for example jjwt-jackson.jar, jjwt-gson.jar or jjwt-orgjson.jar, or your own .jar for custom implementations.
	at io.jsonwebtoken.impl.lang.Services.loadFirst(Services.java:99) ~[jjwt-impl-0.13.0.jar:0.13.0]
	at io.jsonwebtoken.impl.lang.Services.get(Services.java:74) ~[jjwt-impl-0.13.0.jar:0.13.0]
	at io.jsonwebtoken.impl.DefaultJwtBuilder.compact(DefaultJwtBuilder.java:511) ~[jjwt-impl-0.13.0.jar:0.13.0]
	at com.example.spring_security_demo.service.JwtService.generateToken(JwtService.java:49) ~[classes/:na]
	at com.example.spring_security_demo.controller.UserController.login(UserController.java:41) ~[classes/:na]
	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:580) ~[na:na]
```

- The `token` which we generate will have some `claims`
- `Claims`
	- the payload
		- details about the user
			- `username`
			- `expire date`
			- `time of issue`
			- There will be multiple `claims`

```java
@Service
public class JwtService {

	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();

		return Jwts.builder()
		.setClaims(claims)
		.setSubject(username)
		.setIssuedAt(new Date(System.currentTimeMillis()))
		.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 3))
		.signWith(getKey(), SignatureAlgorithm.HS256).compact();

	}
}
```
- we have to generate a key and pass it to `.signWith(Key, SignatureAlgorithm)`
	- basically providing the `STAMP` signature, which will be validated again for each request

- We need to implement the `getKey()` method

---

### Token Generated

- For Generating the key we have to use `cryptographic` libraries like `hmacShaKeyFor()`
- This libraries requires the key to be in `byte[]` format.

```java
private Key getKey() {

	byte[] keyBytes = Decoders.BASE64.decode(secretKey);
	return Keys.hmacShaKeyFor(keyBytes);

}
```


- The `secretKey` will be like our server's private signature
- There are many ways to generate it
	- it can be a hardcoded string
	- `private static final secretKey = thisisasecuresecretekey`
	- but this will be not secure
- We can use a separate method to generate this secretKey and encode id

```java
@Service
public class JwtService {

	private String secretKey;

	public JwtService() {
		secretKey = generateSecretKey();
	}

	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();

		return Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 3))
				.signWith(getKey(), SignatureAlgorithm.HS256).compact();

	}

	private Key getKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

}
```

```bash
POST /login HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Content-Length: 49

{
    "username": "Tim",
    "password": "1234"
}
```

```json
// response
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJUaW0iLCJpYXQiOjE3NzcwNTA5MzksImV4cCI6MTc3NzA1MTExOX0.AJxGu4O9s9GyrURRd5JV2GBhF9GGRVxiu6C653cXKFI
```

![](assets/2026-04-24-23-04-10.png)

- Now while sending a request to secure endpoints say `helloController`
- we can pass this generated `jwt` token in the `Authorization` section `Bearer Token`

![](assets/2026-04-24-23-10-50.png)

---

### Crating a JWT filter

- As of now we are not verifying the `username` and `password` after login and while using other resources
- By default, when we send a request, `Spring Security` uses `UsernamePasswordAuthenticationToken`
	- We should use our own technique
	- we should add a `Security filter` in between
	- Whenever we use `Springframe work` for the web behind the scene it is all servlets
	- We are however running on `Tomcat` and `Tomcat` is a servlet container

![](assets/2026-04-25-14-48-16.png)


![](assets/2026-04-25-14-48-58.png)
* `HttpServletRequest` Object
* `HttpServletResponse` Object
* behind the scenes these objects are there

* We can use filters in between
	- Before we send the request to `servlet`
	- we can make use of these filters
- The request first goes to the servlet container and from here to the `filter` then to servlet

![](assets/2026-04-25-14-53-13.png)

- In the `filter` we can modify the request data
- We can have multiple filters

![](assets/2026-04-25-14-54-25.png)

- By default spring security uses many filters
- For authentication spring security uses `UsernamePasswordFilter`

![](assets/2026-04-25-14-56-07.png)

- Since we are using `jwt`
- We can add a `Jwt` filter

![](assets/2026-04-25-14-56-42.png)

- In the `SpringSecConfig`, where we have implemented `SecurityFilterChain` method
	- there while returning the `http` object we add one more method
	- `.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)`
```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

	return http.csrf(Customizer -> Customizer.disable())
	.authorizeHttpRequests(
		request -> request.requestMatchers("register", "login").permitAll().anyRequest().authenticated())
	.httpBasic(Customizer.withDefaults())
	.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
	.build();
}
```

- In `.addFilterBefore()` we tell before which filter we would like to call our custom filter that would be `UsernamePasswordAuthenticationFilter.class`
- `jwtFilter` will be the object of custom filter class where we will be performing the checks
- We create a new class in config package `JwtFilter` and inject it's object

- Now, we want this `JwtFilter` to be called for all the filters

![](assets/2026-04-25-15-13-17.png)

- To have this capability, we extend our custom class `JwtFilter` with `OncePerRequestFilter`
	- For every request this filter will be called only once hence the name

* `OncePerRequestFilter` filter is an abstract class
- There is only one method that we have to implement `doFilterInternal(HttpServletRequest request, HttpServletResponse response, Filterchain filterChain)`
	- This have three params
	- `Filterchain filterChain` is the object 
- Basically in the `HttpServletRequest request` we are receiving the token

---

### Setting AuthToken in SecurityContext

- We have to get the token from the request `Authorization` header
- Steps
1. From the `Authorization` header first check if we have received `authHeader` and if received check if it starts with `Bearer `
	- We are checking for `Bearer` as the token will be in front of this with after a space
2. If we have received both of them, get the `token` -> will be substring of authHeader("Authorization") from 7 th character
	- We have to extract the userName, we will be using a separate method which will be implemented in `JwtService` class
3. Now, once userName is extracted we have to check if the `Authentication Object` is null
	- We will be using `UsernamePasswordAuthenticationToken` authentication object as we are doing the validation before `UsernamePasswordAuthenticationFilter`
	- `UsernamePasswordAuthenticationToken` accepts three parameters
		1. principal 
		2. credentials
		3. authorities
		- For principal we pass `userDetails` which will be injected through `dependency lookup`
			- we cannot `@Autowired` MyUserDetailsService object as it might cause cyclic dependency
			- `UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(userName);`
		- `credentials` will be null we do not intent to pass credentials
		- `authorities` -> `userDetails.getAuthorities()`
	- If this is empty then that means user is not authenticated, if it is not empty then user is authenticated and we can continue with flow of filterChain
	3.1 If `Authentication object` is empty, we have to create `Authentication object` ourself as `SpringContext` needs this and does not understand jwt
		- First we validate the token using custom method in `jwtService`
	3.2 Once the token is validated, we have to set the `SecurityContext` with the full details of the request.
		- As of now the, `Authentication object`(UsernamePasswordAuthenticationToken) contains only `userDetails`, it does not have full details of the `request`
		- We set the details for `request` using
			- `authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));`
4. We set back the `Authentication object` -> `authToken`
5. Once after the `Authentication object` is set, we have to continue the `filterChain` using `doFilter(request, response)`
	- request and response gotten from doFilterInternal() method 

```java
@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	ApplicationContext context;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String userName = null;

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			userName = jwtService.extractUserName(token);
		}

		if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(userName);

			if (jwtService.validateToken(token, userDetails)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
					userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}

		filterChain.doFilter(request, response);
	}

}
```

---

### Validating Token 

- Basically we need to get the key `(hmacShaKeyFor)` to get the token and then we can extract the values
- All these values are called `claims`
	- We can call all the claims together and use different methods to extract each values
	- example
```java
private Claims extractAllClaims(String token) {
	return Jwts.parser()
	.verifyWith((SecretKey) getKey())
	.build()
	.parseSignedClaims(token)
	.getPayload();
}

public String extractUserName(String token) {
	// extract the username from token
	return extractClaim(token, Claims::getSubject); // getSubject -> getUser
}

private Date extractExpiration(String token) {
	return extractClaim(token, Claims::getExpiration);
}

```
- `private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) `
	- accepts `token` and what type of data you get

- We are calling another method `extractAllClaims(String token)`.
	- In this we are getting the key `.verifyWith((SecretKey) getKey())` from that we are parsing the claims `parseSignedClaims(token)`

- For `validating` the token, what we have to do is 
	- we have to do is whatever `username` we got from table we have to verify that from the `database` 
```java
public boolean validateToken(String token, UserDetails userDetails) {
	final String userName = extractUserName(token);
	return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
}
```
- here `userName` is what we are extracting from token and `userDetails.getUsername()` is from the database
- and we also have to check if the `token` is expired or not
	- for this we first get the `expirationTime` of the jwt from the token and compare it with the current time.
```java
private boolean isTokenExpired(String token) {
	return extractExpiration(token).before(new Date());

}

private Date extractExpiration(String token) {
	return extractClaim(token, Claims::getExpiration);
}
```


```java
@Service
public class JwtService {

	private String secretKey;

	public JwtService() {
		secretKey = generateSecretKey();
	}

	public String generateSecretKey() {
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
			SecretKey secretKey = keyGen.generateKey();
			System.out.println("SecretKey " + secretKey);
			return Base64.getEncoder().encodeToString(secretKey.getEncoded());

		} catch (Exception e) {
			throw new RuntimeException("Error generating the key", e);
		}
	}

	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();

		return Jwts.builder()
		.setClaims(claims)
		.setSubject(username)
		.setIssuedAt(new Date(System.currentTimeMillis()))
		.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 3))
		.signWith(getKey(), SignatureAlgorithm.HS256).compact();
	}

	private Key getKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String extractUserName(String token) {
		// extract the username from token
		return extractClaim(token, Claims::getSubject); // getSubject -> getUser
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);

	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser()
		.verifyWith((SecretKey) getKey())
		.build()
		.parseSignedClaims(token)
		.getPayload();
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String userName = extractUserName(token);
		return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());

	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
}
```

```bash
POST /login HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Content-Length: 52

{
    "username": "Travis",
    "password": "1234"
}
```


```json
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJUcmF2aXMiLCJpYXQiOjE3NzcxNDA3NzUsImV4cCI6MTc3NzE0MDk1NX0.Ze31NiYZJLJvbH0iojXALfD-NHxxAaCJpqKbyvSNBIw
```


```bash
GET /hello HTTP/1.1
Host: localhost:8080
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJUcmF2aXMiLCJpYXQiOjE3NzcxNDA3NzUsImV4cCI6MTc3NzE0MDk1NX0.Ze31NiYZJLJvbH0iojXALfD-NHxxAaCJpqKbyvSNBIw
```

![](assets/2026-04-25-23-44-25.png)

- After 3 minutes when we resend the request we will get 401 Unauthorized as the token has expired

---
