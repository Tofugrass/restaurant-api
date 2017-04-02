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
<!DOCTYPE html>
<html lang="en">
<head>
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
</style>

</head>

<body>




	<div class="container">
	<form id="form" method="post" action="RoundTwo">
		<br>
		<div class="page-header">
			<h1>Meal Madness</h1>
		</div>
		

			<div class="carousel slide" id="myCarousel">
				<div class="carousel-inner">
					<div class="item active">
						<h1 align="center">
							<strong>Round One!</strong>
						</h1>

					<%
						int max = businesses.size();
									if (max < 8) {
										if (max > 3) {
											max = 4;
										}
										else if( max > 1){
											max = 2;
										}
									} else
										max = 8;
					%>
					
						<%
							if(max == 4){ %>
						<script type="text/javascript">
							document.getElementById('form').action = "RoundThree";
						</script>

						<% }else if(max <= 2){ %>
										<script type="text/javascript">
							document.getElementById('form').action = "RoundFour";
						</script>
						
														<%}for (int i = 0; i < max; i++) {
															JSONObject current_bus = (JSONObject) businesses.get(i);
															if (i % 2 == 0) {
																if (i != 0) {
						%>
						<div class="item">
							<h1 align="center">
								<strong>Match <%=i / 2 + 1%></strong>
							</h1>
							<%
							}
								}
						%>

							<div class="col-sm-5">
							<%if (max == 1){%>
								
								<h1 align="center">
								<strong>WINNER!</strong>
							</h1>
								
							<%}%>
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
										<h3>Price</h3>
										<p>
											<%=current_bus.get("price")%></p>
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

										<%
									String reviewHTML = getReviewFromID(
													(String) current_bus.get("id"),
													(String) session.getAttribute("auth"));
											String reviewArr[] = reviewHTML.split("<p lang=\"en\">");
											for (int j = 1; j < 4; j++) {
												reviewArr[j] = reviewArr[j].substring(0,
														reviewArr[j].indexOf("</p>"));
												session.setAttribute("review-"+i+"-"+(j-1),reviewArr[j]);%>
										<input type="hidden" name="review-<%=i%>-<%=(j-1)%>"
											value="<%=reviewArr[j]%>">
										<%
											}
								%>
										<h3>Review 1</h3>
										<p><%=	session.getAttribute("review-"+i+"-"+0)%></p>

										<%
									if (i % 2 == 0) {
								%>

										<div class="radio">
											<label><input type="radio"
												name="optradio<%=i / 2 + 1%>" checked="checked"
												id="radio<%=i%>" style="transform: scale(2);" value="<%=i%>"><strong>Winner</strong></label>
										</div>
										<%
										}
									%>

										<%
										if (i % 2 != 0) {
									%>
										<div class="radio">
											<label><input type="radio"
												name="optradio<%=i / 2 + 1%>" id="radio<%=i%>"
												style="transform: scale(2);" value="<%=i%>"><strong>Winner</strong></label>
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
							style="display: flex; vertical-align: middle; justify-content: center; align-items: center;">
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
					class="btn btn-primary">GO TO ROUND 2</button>
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
<%!public String getReviewFromID(String ID, String auth) {
		String url = "https://api.yelp.com/v3/businesses/" + ID + "/reviews";
		//String url = "https://www.yelp.com/biz/a-pig-in-a-fur-coat-madison?hrid=borexsbr6a_k409-J9nlJw&adjust_creative=iJUPQDxk-6lz7gz5CpfMCQ&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_reviews&utm_source=iJUPQDxk-6lz7gz5CpfMCQ";

		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet getRequest = new HttpGet(url);

		getRequest.setHeader("Content-Type",
				"application/x-www-form-urlencoded");
		getRequest
				.setHeader(
						"User-Agent",
						"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");
		getRequest.setHeader("Authorization", auth);
		try {
			CloseableHttpResponse response = client.execute(getRequest);
			//System.out.println(getRequest.getURI());
			String responseString = EntityUtils.toString(response.getEntity());
			//System.out.println(responseString);
			client.close();
			response.close();
			JSONObject json = (JSONObject) (new JSONParser()
					.parse(responseString));
			JSONArray reviews = (JSONArray) json.get("reviews");
			json = (JSONObject) reviews.get(0);
			url = (String) json.get("url");
			client = HttpClients.createDefault();
			getRequest = new HttpGet(url);

			getRequest.setHeader("Content-Type",
					"application/x-www-form-urlencoded");
			getRequest
					.setHeader(
							"User-Agent",
							"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");
			getRequest.setHeader("Authorization", auth);
			response = client.execute(getRequest);
			//System.out.println(getRequest.getURI());
			responseString = EntityUtils.toString(response.getEntity());
			//System.out.println(responseString);
			client.close();
			response.close();
			return responseString;
		} catch (ClientProtocolException e) {
			System.out.println("ClientProtocolException");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("ParseException");
			e.printStackTrace();
		}
		return null;
	}%>