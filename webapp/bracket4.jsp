<!DOCTYPE html>
<html lang="en">
<head>
<%@ page import="java.util.*"%>
<%@ page import="javax.servlet.http.HttpServletRequest"%>
<%@ page import="javax.servlet.http.HttpServletResponse"%>
<%@ page import="java.io.IOException"%>
<%@ page import="org.json.simple.JSONArray"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="org.json.simple.parser.JSONParser"%>
<%@ page import="org.apache.http.client.ClientProtocolException"%>
<%@ page import="org.apache.http.client.methods.CloseableHttpResponse"%>
<%@ page import="org.apache.http.client.methods.HttpGet"%>
<%@ page import="org.apache.http.impl.client.CloseableHttpClient"%>
<%@ page import="org.apache.http.impl.client.HttpClients"%>
<%@ page import="org.apache.http.util.EntityUtils"%>
<%@ page import="java.io.FileWriter"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="org.json.simple.parser.ParseException"%>
<%
	if (session.getAttribute("businesses") == null) {
		response.sendRedirect("http://localhost:8080/j2eeapplication/index.jsp");
	}
	JSONArray businesses = (JSONArray) session
			.getAttribute("businesses");
	if (businesses == null) {
		session.setAttribute("businesses", "null");
	}
%>

<meta charset="UTF-8">

<!-- If IE use the latest rendering engine -->
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<!-- Set the page to the width of the device and set the zoom level -->
<title>Meal Madness WINNER</title>
<link rel="stylesheet" type="text/css"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">

<style>
body{
background-color: #57DAE6;
}
.jumbotron {
	background-color: #2E2D88;
	color: white;
}
/* Adds borders for tabs */
.tab-content {
	border-left: 1px solid #ddd;
	border-right: 1px solid #ddd;
	border-bottom: 1px solid #ddd;
	padding: 10px;
}

.nav-tabs {
	margin-bottom: 100;
}

.thumbnail img {
	max-width: 80%; /* do not stretch the bootstrap column */
}

.img-wrapper {
	position: relative;
	padding-bottom: 80%;
	overflow: hidden;
	width: 80%;
}

.img-wrapper img {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
}

/* Mobile Only */
@media ( max-width : 767px) {
	.page-header,.control-box {
		text-align: center;
	}
}

@media ( max-width : 479px) {
	.caption {
		word-break: break-all;
	}
}

</style>

</head>

<body background="https://images.freecreatives.com/wp-content/uploads/2016/02/Dark-Hardwood-Floor-Background.jpg">
	<div class="container">
		<br>
		<div class="page-header">
			<h1 style="color: white;">Meal Madness</h1>
		</div>

					<div class="item active">
						<h1 align="center" style="color: white;">
							<strong>WINNER!</strong>
						</h1>
   
						<%
						JSONObject current_bus = (JSONObject) businesses.get(Integer.parseInt((String)session.getAttribute("winner1")));
							
						%>
	<div class="col-lg-2 col-md-2" align="center">
	</div>
							<div class="col-sm-12 col-lg-8 col-md-8" align="center">
								<div class="img-wrapper" >
								
									<a href="#"><img src="<%=current_bus.get("image_url")%>"
										alt="" class="img-thumbnail"></a>
								</div>
								<div class="caption" >
								<div class="well" style="width:75%;">
									<h2>
										<%=current_bus.get("name")%>
									</h2>

									<h3>Rating</h3>
									<p>
										<%=current_bus.get("rating")%></p>
										<%
										if(current_bus
										.get("price") != null){%>
									<h3>Price</h3>
									<p>
										<%=current_bus.get("price")%></p><%}%>
									<%
										if(current_bus
										.get("distance") != null){%>
											<h3>Distance</h3>
											<p>
												<%=((double) Math.round((Double) current_bus
								.get("distance") / 1609 * 100d) / 100d)%>
												Miles
											</p>
										<%	
										}
										%>
									
									<h3>Contact</h3>
									<p><%=current_bus.get("display_phone")%></p>

									
									
									<!-- //TODO: print map 
									
									<p><%=session.getAttribute("location")%></p>-->
									<img alt="Map" height="135" src="<%= session.getAttribute("restMap"+(Integer.parseInt((String)session.getAttribute("winner1"))))%>" width="286">

									<a class="btn btn-mini"
										href="<%=(String) current_bus.get("url") %>" target="_blank">�
										Read More</a>
								</div>
								</div>
								
							</div>				

					</div>

		<script
			src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
		<script
			src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
	</div>

</body>
</html>
