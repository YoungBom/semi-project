<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="true"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">

<title>🍔 BurgerHub — 내 주변 매장</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<link href="resources/css/findStore.css" rel="stylesheet">
<link href="resources/css/main.css" rel="stylesheet">

<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=2890edbb91db82862429679da4bd158c&libraries=services"></script>
</head>
<body>
<%@ include file="/include/header.jsp" %>
	
  <h2>📍 BurgerHub — 내 주변 패스트푸드 매장</h2>
  <div id="map"></div>
  <div id="list"></div>

  <!-- ✅ 화면 좌하단 고정 버튼 -->
  <button id="myLocationBtn" title="내 위치로 이동">📍</button>
<%@ include file="/include/footer.jsp" %>

<script>
(function(){
	const icons = {
	  "버거킹": "/semi-project/image/burgerkingMarker.png",
	  "롯데리아": "/semi-project/image/lotteriaMarker.png",
	  "맥도날드": "/semi-project/image/mcdonaldMarker.png"
	};
  const brands = ["버거킹", "롯데리아", "맥도날드"];

  const map = new kakao.maps.Map(document.getElementById('map'), {
    center: new kakao.maps.LatLng(37.5665,126.9780),
    level: 5
  });

  const ps = new kakao.maps.services.Places();
  const listContainer = document.getElementById("list");

  let userLoc = null;
  let userMarker = null;
  let circle = null;
  let openInfoWindow = null;
  let cards = [];

  // ✅ 내 위치 버튼 클릭
  document.getElementById("myLocationBtn").addEventListener("click", ()=>{
    if(userLoc){
      map.panTo(userLoc);
      jumpMarker(userMarker);
    } else {
      alert("현재 위치 정보를 불러오는 중입니다.");
    }
  });

  // ✅ 내 위치 가져오기
  if(navigator.geolocation){
    navigator.geolocation.getCurrentPosition(onSuccess, onError, { enableHighAccuracy:true, timeout:10000 });
  } else {
    alert("브라우저가 위치 정보를 지원하지 않습니다.");
  }

  function onSuccess(pos){
    const lat = pos.coords.latitude;
    const lng = pos.coords.longitude;
    userLoc = new kakao.maps.LatLng(lat, lng);
    map.setCenter(userLoc);

    userMarker = new kakao.maps.Marker({
      map: map,
      position: userLoc,
      title: "내 위치"
    });

    circle = new kakao.maps.Circle({
      center: userLoc,
      radius: 7000,
      strokeWeight: 2,
      strokeColor: '#3a7afe',
      strokeOpacity: 0.6,
      fillColor: '#3a7afe',
      fillOpacity: 0.08
    });
    circle.setMap(map);

    brands.forEach(b=>searchBrand(b, userLoc, 7000));
  }

  function onError(){
    alert("위치 정보를 가져올 수 없어 서울 중심으로 표시합니다.");
    const center = map.getCenter();
    brands.forEach(b=>searchBrand(b, center, 7000));
  }

  // ✅ 장소 검색
  function searchBrand(brand, center, radius){
    ps.keywordSearch(brand, (data,status,pagination)=>{
      if(status===kakao.maps.services.Status.OK){
        data.forEach(p=>displayMarker(p,brand));
        if(pagination.hasNextPage) pagination.nextPage();
      }
    }, { location:center, radius:radius });
  }

  // ✅ 마커 & 카드 생성
  function displayMarker(place, brand){
    const marker = new kakao.maps.Marker({
      map: map,
      position: new kakao.maps.LatLng(place.y, place.x),
      image: new kakao.maps.MarkerImage(icons[brand], new kakao.maps.Size(50,50))
    });

    const addr = place.road_address_name || place.address_name || "";
    const phone = place.phone ? "📞 " + place.phone : "";

    const info = new kakao.maps.InfoWindow({
      content:
        `<div style="padding:10px;font-size:13px;width:220px;">
          <strong style="color:#ff6600;">${brand}</strong><br>
          ${place.place_name}<br>
          <small>${addr}</small><br>
          ${phone ? `<small>${phone}</small>` : ""}
        </div>`
    });

    // ✅ 카드 생성
    const card = document.createElement("div");
    card.className = "card";
    card.innerHTML = `
      <div class="brand">${brand}</div>
      <div class="store">${place.place_name}</div>
      <div class="addr">${addr}</div>
      <div class="phone">${phone}</div>
    `;
    listContainer.appendChild(card);
    cards.push(card);

    // ✅ 카드 클릭 시 지도 이동 + 인포윈도우 오픈 + 스크롤 업
    card.addEventListener("click", ()=>{
      const pos = new kakao.maps.LatLng(place.y, place.x);
      map.panTo(pos);
      if(openInfoWindow) openInfoWindow.close();
      info.open(map, marker);
      openInfoWindow = info;
      jumpMarker(marker);
      setActiveCard(card);

      // 지도 위치로 스크롤 이동
      window.scrollTo({
        top: document.getElementById('map').offsetTop - 50,
        behavior: "smooth"
      });
    });

    // ✅ 마커 클릭 시 카드 동기화
    kakao.maps.event.addListener(marker, 'click', ()=>{
      if(openInfoWindow) openInfoWindow.close();
      info.open(map, marker);
      openInfoWindow = info;
      jumpMarker(marker);
      setActiveCard(card);
    });
  }

  // ✅ 카드 하이라이트
  function setActiveCard(active){
    cards.forEach(c=>c.classList.remove("active"));
    active.classList.add("active");
    active.scrollIntoView({ behavior:"smooth", block:"center" });
  }

  // ✅ 마커 점프 애니메이션
  function jumpMarker(marker){
    const pos = marker.getPosition();
    let t = 0;
    const jump = setInterval(()=>{
      t += 1;
      const lat = pos.getLat() - Math.sin(t/5)*0.0003;
      marker.setPosition(new kakao.maps.LatLng(lat, pos.getLng()));
      if(t > 30){ clearInterval(jump); marker.setPosition(pos); }
    }, 16);
  }

})();
</script>
</body>
</html>
