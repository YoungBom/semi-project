<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="true"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>🍔 BurgerHub — KM타워 기준 주변 매장</title>
<style>
  body { background:#fffaf0; font-family:'Noto Sans KR', sans-serif; }
  #map { width:90%; height:520px; margin:30px auto; border-radius:12px; box-shadow:0 4px 10px rgba(0,0,0,.1); }
  h2 { text-align:center; color:#ff6600; }
</style>
<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=2890edbb91db82862429679da4bd158c&libraries=services"></script>
</head>
<body>
  <h2>📍 BurgerHub — KM타워 주변 패스트푸드 매장</h2>
  <div id="map"></div>

<script>
(function(){
  var icons = {
    "버거킹": "https://img.icons8.com/color/48/hamburger.png",
    "롯데리아": "https://img.icons8.com/color/48/french-fries.png",
    "맥도날드": "https://img.icons8.com/color/48/cheeseburger.png"
  };
  var brands = ["버거킹","롯데리아","맥도날드"];

  // ✅ KM타워 수동 좌표
  var kmTowerLat = 37.4764610396654;
  var kmTowerLng = 126.879842463241;

  var mapContainer = document.getElementById('map');
  var map = new kakao.maps.Map(mapContainer, {
    center: new kakao.maps.LatLng(kmTowerLat, kmTowerLng),
    level: 5
  });

  // ✅ 내 위치 마커 (KM타워)
  var myMarker = new kakao.maps.Marker({
    map: map,
    position: new kakao.maps.LatLng(kmTowerLat, kmTowerLng),
    title: "현재 위치 (KM타워)"
  });

  // ✅ 반경 표시 원
  var circle = new kakao.maps.Circle({
    center: new kakao.maps.LatLng(kmTowerLat, kmTowerLng),
    radius: 600000, // 7km
    strokeWeight: 2,
    strokeColor: '#3a7afe',
    strokeOpacity: 0.6,
    fillColor: '#3a7afe',
    fillOpacity: 0.08
  });
  circle.setMap(map);

  // ✅ 카카오 장소 검색
  var ps = new kakao.maps.services.Places();
  var openInfoWindow = null;

  brands.forEach(function(brand){
    searchBrand(brand, new kakao.maps.LatLng(kmTowerLat, kmTowerLng), 7000);
  });

  function searchBrand(brand, center, radius){
    ps.keywordSearch(brand, function(data, status, pagination){
      if (status === kakao.maps.services.Status.OK) {
        for (var i = 0; i < data.length; i++) {
          displayMarker(data[i], brand);
        }
        if (pagination.hasNextPage) pagination.nextPage();
      }
    }, { location: center, radius: radius });
  }

  function displayMarker(place, brand){
    var marker = new kakao.maps.Marker({
      map: map,
      position: new kakao.maps.LatLng(place.y, place.x),
      image: new kakao.maps.MarkerImage(icons[brand], new kakao.maps.Size(40,40))
    });

    var addr = place.road_address_name || place.address_name || "";
    var phone = place.phone ? "📞 " + place.phone : "";

    var content =
      '<div style="padding:10px;font-size:13px;width:220px;">' +
      '<strong style="color:#ff6600;">' + brand + '</strong><br>' +
      place.place_name + '<br>' +
      '<small>' + addr + '</small><br>' +
      (phone ? '<small>' + phone + '</small>' : '') +
      '</div>';

    var infowindow = new kakao.maps.InfoWindow({ content: content });
    kakao.maps.event.addListener(marker, 'click', function(){
      if (openInfoWindow) openInfoWindow.close();
      infowindow.open(map, marker);
      openInfoWindow = infowindow;
    });
  }
})();
</script>
</body>
</html>
