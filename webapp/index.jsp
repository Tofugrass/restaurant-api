<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">

<!-- If IE use the latest rendering engine -->
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<!-- Set the page to the width of the device and set the zoom level -->
<meta name="viewport" content="width = device-width, initial-scale = 1">
<title>Meal Madness</title>
<link rel="stylesheet" type="text/css"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">

    <!-- Bootstrap core CSS -->
    <link href="../../dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <link href="../../assets/css/ie10-viewport-bug-workaround.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="cover.css" rel="stylesheet">
<style>
h1{
	color: white;
}
h2{
	color: white;
}
h4{
	color: white;
}
h5{
	color: white;
}
.jumbotron {
   background: rgb(200, 54, 54); /* This is for ie8 and below */
   background: rgba(200, 54, 54, 0.01); 
   	padding: 10px;
   	color: white;
}
.container {
   background: rgb(80, 85, 86); /* This is for ie8 and below */
   background: rgba(80, 85, 86, 0.10); 
}

</style>

</head>

<body background="https://images.freecreatives.com/wp-content/uploads/2016/02/Dark-Hardwood-Floor-Background.jpg">
<br><br>
	<!--  <form action="FirstServlet" method="post"> -->

	<!-- secret: b1vKukbfvjb9hWeQXdai1sOqRH9Xqof2KEg2cjTwQ445tb2cLAAPPrvgqipF1iH5 
 app id: iJUPQDxk-6lz7gz5CpfMCQ -->
	<div class="container">
		<br>

		<div class="jumbotron">
			<h1 align="center">Welcome To Meal Madness</h1>
			<div class="well" style="color:grey">
			<h3>How to play:</h3>
			<ol>
			<li>After you enter your info, you will get to start filling out your bracket. </li>
			<li>Round one you only get a review. </li>
			<li>Round two you get a new review and a map.</li>
			<li>Round three you get one new review. </li>
			<li>You should select the winner based only on the available info.</li>
			</ol>
			</div>
		</div>
		<!-- <div class="well" style="background-color:#505556"> -->
		<h4 align="center">
			<font face="Arial" color="#fffff" size="15">Let's find you a
				restaurant!</font>
		</h4>

		<h3 style="color: white;">Do you prefer delivery, or are you a
			fan of the ambiance?</h3>
		<form method="post" action=FirstServlet>
			<div class="form-group">
				<select class="form-control" id="delivery" name="delivery"
					style="color: black;">
					<option>Dine-in</option>
					<option>Delivery</option>
				</select>
			</div>

			<p align="justify" style="color: white;">How far are you willing
				to travel?</p>

			<input name="distance" type="range" min="0" max="20" value="0"
				step="2" onchange="showValue(this.value)" style="color: white;" /> <span
				id="range" style="color: white;">Within a mile</span>
			<script type="text/javascript">
				function showValue(newValue) {
					if (newValue == 0)
						document.getElementById("range").innerHTML = "Within a mile";
					else
						document.getElementById("range").innerHTML = newValue
								+ " Miles";
				}
			</script>
			<br> <br>
			<p id="locationMsg" style="color: white"></p>

			<input type="hidden" id="location" name="location" value="null"></input>

			<button type="button" onclick="getLocation()" class="btn btn-lg">Find
				Restaurants Using My Location</button>
			<br> <br>
			<div id="mapholder"></div>
			<script
				src="https://maps.google.com/maps/api/js?sensor=false&key=AIzaSyAdij3-MFFUssJ1dhXyF_LxQfadSVMbvgE"></script>

			<script>
				var x = document.getElementById("locationMsg");
				function getLocation() {
					if (navigator.geolocation) {
						navigator.geolocation.getCurrentPosition(showPosition,
								showError);
					} else {
						x.innerHTML = "Geolocation is not supported by this browser.";
					}
				}
				function showPosition(position) {
					lat = position.coords.latitude;
					lon = position.coords.longitude;
					latlon = new google.maps.LatLng(lat, lon);
					document.getElementById("location").setAttribute("value", lat+","+ lon);
					mapholder = document.getElementById('mapholder');
					x.innerHTML = "If your location isn't very accurate, just increase the  search radius";
					mapholder.style.height = '250px';
					mapholder.style.width = '500px';
					
					var myOptions = {
						center : latlon,
						zoom : 14,
						mapTypeId : google.maps.MapTypeId.ROADMAP,
						mapTypeControl : false,
						navigationControlOptions : {
							style : google.maps.NavigationControlStyle.SMALL
						}
					};
					var map = new google.maps.Map(document.getElementById("mapholder"),myOptions);
					new google.maps.Marker({position:latlon,map:map,title:"You are here!"});
				}
				function showError(error) {
					document.getElementById("zipTitle").setAttribute("style", "display: inline;");
					document.getElementById("zipInput").setAttribute("style", "display: block;");
					
					switch (error.code) {
					case error.PERMISSION_DENIED:
						x.innerHTML = "User denied the request for Geolocation.";
						break;
					case error.POSITION_UNAVAILABLE:
						x.innerHTML = "Location information is unavailable.";
						break;
					case error.TIMEOUT:
						x.innerHTML = "The request to get user location timed out.";
						break;
					case error.UNKNOWN_ERROR:
						x.innerHTML = "An unknown error occurred.";
						break;
					}
				}
			</script>
			<br>
 <div class="input-group">
			<span  id="zipTitle" class="input-group-addon" id="sizing-addon2" style="display: none;">Your
					Zip</span>	 
				<input style="display: none;" name="zip" id="zipInput"
					type="number" class="form-control" placeholder="54915">	
			</div>
			<br>
			<p style="color: white;">Here you can enter an optional search
				term</p>
				<input  name="term" type="text" class="form-control" placeholder="Restaurant">
			<br>
		
			<button type="submit" class="btn-md">Let's get Started</button>
			<br>
			<br>
		</form>
	</div>
	<!-- </div> -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
</body>
</html>