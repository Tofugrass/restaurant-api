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
<title>Meal Madness Round 1</title>
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
	max-width: 100%; /* do not stretch the bootstrap column */
}

.img-wrapper {
	position: relative;
	padding-bottom: 100%;
	overflow: hidden;
	width: 100%;
}

.img-wrapper img {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
}
/* Carousel Control */
.control-box {
	text-align: right;
	width: 100%;
}

.carousel-control {
	background: #666;
	border: 0px;
	border-radius: 0px;
	display: inline-block;
	font-size: 34px;
	font-weight: 200;
	line-height: 18px;
	opacity: 0.5;
	padding: 4px 10px 0px;
	position: static;
	height: 30px;
	width: 15px;
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
<%for (int i = 0; i < 4; i++){%>
	#map<%=i%> {
		  height: 400px;
		  width: 100%;
		 }
<%}%>

</style>

</head>

<body background="https://images.freecreatives.com/wp-content/uploads/2016/02/Dark-Hardwood-Floor-Background.jpg">




	<div class="container">
		<br>
		<div class="page-header">
			<h1 style="color: white;">Meal Madness</h1>
		</div>
		<form method="post" action=RoundThree>

			<div class="carousel slide" id="myCarousel">
				<div class="carousel-inner">
				<h1 align="center" style="color: white;">
							<strong>Round of 4</strong>
						</h1>
					<div class="item active">
						

   
						<%
						
						for (int i = 0; i < 4; i++) {
							JSONObject current_bus = (JSONObject) businesses.get(Integer.parseInt((String)session.getAttribute("winner"+(i+1))));
							if (i % 2 == 0) {
								if (i != 0) {
%>
<div class="item">

<%
}%>
								<h2 align="center" style="color: white;">
								Match <%=i / 2 + 1%>
							</h2>
<%}
						%>

							<div class="col-sm-5">
								<div class="img-wrapper">
									<a href="#"><img src="<%=current_bus.get("image_url")%>"
										alt="" class="img-thumbnail"></a>
								</div>
								<div class="caption">
								<div class="well">
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

									
									<h3>Review 2</h3>
										<p><%=	session.getAttribute("review-"+Integer.parseInt((String)session.getAttribute("winner"+(i+1)))+"-"+1)%></p>
									<!-- //TODO: print map 
									
									<p><%=session.getAttribute("location")%></p>-->
									<img alt="Map" height="135" src="<%= session.getAttribute("restMap"+(Integer.parseInt((String)session.getAttribute("winner"+(i+1)))))%>" width="286">
									
									<%
									if (i % 2 == 0) {
								%>

									<div class="radio">
										<label><input type="radio"
											name="optradio<%=i / 2 + 1%>" checked="checked"
											id="radio<%=i%>" style="transform: scale(2);"
											value="<%=Integer.parseInt((String)session.getAttribute("winner"+(i+1)))%>"><strong>Winner</strong></label>
									</div>
									<%
										}
									
									else{
									%>
									<div class="radio">
										<label><input type="radio"
											name="optradio<%=i / 2 + 1%>" id="radio<%=i%>"
											style="transform: scale(2);" value="<%=Integer.parseInt((String)session.getAttribute("winner"+(i+1)))%>"><strong>Winner</strong></label>
									</div>

									<%
									}
								%>
								</div>
								</div>
							</div>

							<%
							if (i % 2 != 0) {
						%>

						</div>

						<%
						} else {
					%>
						<div class="col-sm-1"
							style="display: flex; vertical-align: middle; justify-content: center; align-items: center; color:white;">
							<h2>VS</h2>
						</div>
						<%
						}}
					%>

					</div>


					<nav>
						<ul class="control-box pager">
							<li><a data-slide="prev" href="#myCarousel" class=""><i
									class="glyphicon glyphicon-chevron-left"></i></a></li>
							<li><a data-slide="next" href="#myCarousel" class=""><i
									class="glyphicon glyphicon-chevron-right"></i></a></li>
						</ul>
					</nav>
					<!-- /.control-box -->
				</div>
				<!-- /#myCarousel -->

			</div>

			<div class="text-center">
				<button type="submit" value="Send"
					class="btn btn-primary">GO TO FINALS</button>
				<br>
				<br>
				<br>
			</div>
		</form>

		<script
			src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
		<script
			src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
	</div>

</body>
</html>
