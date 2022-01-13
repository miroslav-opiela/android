# Map Tutorial

Aplikácia, ktorá demonštruje prácu s google play services a google mapou.

## 1. Google Maps Activity

- vytvoríme nový projekt s predpripravenou aktivitou obsahujúcou Google mapu (v build gradle je pridané dependency na `play-services-maps`)

- do `google_maps_api.xml` doplníme [API KEY](https://support.google.com/googleapi/answer/6158862?hl=en) (všimnite si, že ide o xml v konfigurácii debug, rovnako si môžete všimnúť meta-data v manifeste)

  - v google console vyrobiť projekt, povoliť google mapy a kopírovať API KEY (v credentials)

- v emulátore (alebo v telefóne) sa prihlásime do google play s rovnakým účtom ako v google console

- vyskúšame rôzne nastavenia mapy - `mapType`, `isTrafficEnabled`, ...

  ``` kotlin
  mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
  mMap.isTrafficEnabled = true
  mMap.moveCamera(CameraUpdateFactory.zoomTo(18f))
  ```

## 2. Location Service

* Location Service je služba poskytovaná google play services
* `implementation 'com.google.android.gms:play-services-location:19.0.1'`
  * môžeme tiež aktualizovať verziu `play-services-maps`
* [FusedLocationProviderClient](https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderClient) poskytuje metódy na zistenie aktuálnej pozície
  * `LocationServices.getFusedLocationProviderClient(this)`
* `getCurrentLocation` - aktívne zistí a vráti aktuálnu pozíciu. Ak to nie je možné v priebehu pár sekúnd, vráti null.
* `getLastLocation` - vráti poslednú (cache-ovanú) informáciu o polohe. 
* Google play services realizujú asynchrónne operácie pomocou abstraktnej triedy `Task`
  * využijeme: `addOnCompleteListener`, `isSuccessful`, `result`
* Lokalizácia v aplikácii
  * pridať Floating Action Button - Constraint Layout a v ňom FAB a Fragment s mapou
  * onClick - uložiť aktuálnu LatLng alebo Location (obsahuje viac informácii)
  * prekresliť mapu po lokalizácii - vymazať aktuálny stav, nakresliť marker, presunúť kameru

## 3. Location Permissions

* [Permissions dokumentácia](https://developer.android.com/training/location/permissions)
* Rovnaký princíp ako pri ostatných runtime permissions. Na základe verzie Androidu môže byť možnosť aj jednorázového povolenia location a pod.
* Foreground vs Background location
  * foreground odporúčaný foreground service (budeme sa tomu venovať pri inej aplikácii)
* Precise (Fine) vs. Approximate (Coarse) location
  * odporúča sa použiť coarse permission ak to aplikácii stačí
* Vysvetlenie k procesu lokalizácie: `getLastLocation` aktívne nevyhľadáva pozíciu. Na telefóne môžeme mať null. Odporúčam zapnúť google mapu a povoliť zistenie polohy. V kóde by bolo potrebné urobiť [request](https://developer.android.com/training/location/request-updates). V emulátore je možné nastaviť pozíciu manuálne v nastaveniach emulátora.

## 4. Ďalšia práca s mapou

* Geocoder umožňuje konverziu medzi adresou a GPS koordinátmi
* môžeme pridať nový FAB na vyžiadanie adresy
* preskúmajme listener na mape (napr. vyrobiť marker po kliknutí a nastaviť aktuálnu pozíciu - aby sa potom zistila adresa)

### Bonusové zdroje

* [Codelab](https://developers.google.com/codelabs/maps-platform/maps-platform-101-android#6) - návod na prácu s mapou vrátane vlastného layoutu pre marker a clustrovanie
* [Geocoding app](https://betterprogramming.pub/reverse-geocoding-with-google-maps-in-android-313bed159817) - posúvanie mapky a automatické zisťovanie adresy



