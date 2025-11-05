(function(){
  const CTX = window.CTX;

  // ✅ 브랜드별 마커 이미지
  const icons = {
    "버거킹": `${CTX}/img/burgerkingMarker.png`,
    "롯데리아": `${CTX}/img/lotteriaMarker.png`,
    "맥도날드": `${CTX}/img/mcdonaldMarker.png`
  };

  const brands = ["버거킹", "롯데리아", "맥도날드"];

  // ✅ 지도 초기화
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

  // ✅ 내 위치 버튼 클릭 → 중앙 포커스
  document.getElementById("myLocationBtn").addEventListener("click", ()=>{
    if(userLoc){
      map.panTo(userLoc);
      jumpMarker(userMarker);
      smoothScrollToMap();
    } else {
      alert("현재 위치 정보를 불러오는 중입니다.");
    }
  });

  // ✅ 현재 위치 가져오기
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

  // ✅ 브랜드별 매장 검색
  function searchBrand(brand, center, radius){
    ps.keywordSearch(brand, (data,status,pagination)=>{
      if(status===kakao.maps.services.Status.OK){
        // 🔥 1️⃣ 불필요한 단어 필터링
        const filtered = data.filter(p => !/(주차장|공중화장실|ATM|세차장|편의점|센터|사무소|관리소|입구|개방화장실)/.test(p.place_name));

        // 🔥 2️⃣ 브랜드명이 포함된 정상 매장만
        const brandFiltered = filtered.filter(p => p.place_name.includes(brand));

        // 🔥 3️⃣ displayMarker 호출
        brandFiltered.forEach(p=>displayMarker(p,brand));

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
      removable: false,
      content: `
        <div style="padding:10px;font-size:13px;width:220px;">
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

    // ✅ 카드 클릭 시 → 지도 이동 + 인포윈도우 오픈 + 지도 포커스
    card.addEventListener("click", ()=>{
      const pos = new kakao.maps.LatLng(place.y, place.x);
      map.panTo(pos);
      if(openInfoWindow) openInfoWindow.close();
      info.open(map, marker);
      openInfoWindow = info;
      jumpMarker(marker);
      setActiveCard(card, true); // true = scroll to map
    });

    // ✅ 마커 클릭 시 → 카드 강조만, 스크롤은 유지
	kakao.maps.event.addListener(marker, 'click', ()=>{
	  // ✅ 기존 열린 인포윈도우 닫기
	  if(openInfoWindow) openInfoWindow.close();

	  // ✅ 새 인포윈도우 열기
	  info.open(map, marker);
	  openInfoWindow = info;

	  // ✅ 마커 점프 애니메이션
	  jumpMarker(marker);

	  // ✅ 지도 중심을 해당 마커로 이동시키기 (핵심)
	  const pos = marker.getPosition();
	  map.panTo(pos); // 부드럽게 이동
	  setTimeout(() => map.setCenter(pos), 300); // 🔥 완전히 중앙 정렬

	  // ✅ 해당 매장의 카드 활성화
	  const cardIndex = cards.findIndex(c => c.querySelector(".store").textContent === place.place_name);
	  if(cardIndex >= 0) setActiveCard(cards[cardIndex], false); // false = 스크롤 안 함
	});

    // ✅ 지도 빈 곳 클릭 시 → 풍선 닫기 + 카드 포커스 해제
    kakao.maps.event.addListener(map, 'click', ()=>{
      if(openInfoWindow){
        openInfoWindow.close();
        openInfoWindow = null;
        cards.forEach(c=>c.classList.remove("active"));
      }
    });
  }

  // ✅ 카드 하이라이트 (scroll 여부 제어 가능)
  function setActiveCard(active, scrollToMap){
    cards.forEach(c=>c.classList.remove("active"));
    active.classList.add("active");

    // 카드 클릭 시에만 지도 쪽으로 스크롤 이동
    if(scrollToMap) {
      smoothScrollToMap();
    }
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

  // ✅ 부드럽게 지도 쪽으로 스크롤 이동
  function smoothScrollToMap(){
    window.scrollTo({
      top: document.getElementById('map').offsetTop - 80,
      behavior: "smooth"
    });
  }

})();
