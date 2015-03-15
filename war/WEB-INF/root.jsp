<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Assignment 001</title>
</head>
	<body>
  		<c:choose>
  			<c:when test="${user !=null.email}">
  			
  				<p>Welcome ${user.email}</br>
  					You are logged in</br> 
  					You can logout <a href="${logout_url}">here</a></br>
  					
  					</br>
  					</br>
  					
  					
  					You can search for an anagram of your word here</br> 
  					<form action="/anagramservlet" method="get" >
						<input type="text" name="search_word"/> 
						<input type="submit" value="submit"/> 
  					</form>
  					</br> 
  					You can add a word here.
  					</br>
  					</br>
  					<form action="/anagramservlet" method="post" >
						<input type="text" name="add_word"/> 
						<input type="submit" value="submit"/> 
  					</form>
					</br>
  					</br>
  							${message}
  					 
  				</p>
  			</c:when>
  			<c:otherwise>
  				<p>
  					Welcome to the Anagram App!</br> 
  					<a href="${login_url}">Sign in here</a> 
  			</c:otherwise> 
  		</c:choose>
	</body>
</html>