
<html xmlns:th="https://www.thymeleaf.org">
	<body>
		<h2>Welcome to Tutorial</h2>
		<p th:text="${student}">${student}</p>
		<p th:text="'Welcome to the ' + ${course} + ' Tutorial, Have a nice day!'"/>
	</body>
</html>

