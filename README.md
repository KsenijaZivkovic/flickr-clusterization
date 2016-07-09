# 1. Klasterizacija i pretraživanje slika sa Flickr-a
Korišćenjem Flickr API-ja aplikacija preuzima podatke o slikama, klasterizuje ih na osnovu naziva i tagova slika i prikazuje slike po pronađenim klasterima.
# 2. DataSet
Podaci nad kojima se radi klasterizacija prikupljeni su sa [Flickr](http://www.flickr.com)-a.
Za prikupljanje podataka korišćene su sledeće metode:
* **flickr.photos.search**, koja vraća listu slika koje zadovoljavaju kriterijum pretrage.
Korišćeni parametri metode:
 * api_key, ključ aplikacije
 * text, tekst pretrage
 * tags, tagovi
 * per_page, broj rezultata po strani
 * content_type, tip sadržaja(fotografija, screenshot ili ostalo)
 * license, id licenci fotografija
 * format, format odgovora na zahtev
* **flickr.photos.getInfo**, koja vraća informacije o fotografiji. Korišćeni parametri:
 * api_key
 * photo_id
 * format
* **flickr.photos.getSizes**, koja vraća linkove ka različitim veličinama fotografija. Korišćeni parametri:
 * api_key
 * photo_id
 * format  
  
Primer poziva servisa:  
https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=acaedd637926848183cdc5b671127de3&tags=cat&text=cat&per_page=500&content_type=1&license=1,2,3,4,5,6&format=json  
Primer odgovora na poziv servisa:  
![JSON odgovor](https://github.com/KsenijaZivkovic/flickr-clusterization/blob/update/screenshots/jsonodgovor.JPG?raw=true)  
Prikupljeni podaci čuvaju se u fajlu data/slike.arff.  
Atributi koji se čuvaju su: id, link ka slici, naziv slike, tagovi i broj klastera kome slika pripada, koji je inicijalno nepoznat i vrednost mu se dodeljuje nakon izvršene klasterizacije.  
![arff fajl](https://github.com/KsenijaZivkovic/flickr-clusterization/blob/update/screenshots/arff.JPG?raw=true)  
  
# 3. EM klasterovanje
Klasterovanje je određivanje pripadnosti objekata različitim grupama, tako da u svakoj grupi budu objekti koji dele neke zajedničke osobine. Expectation Minimization (EM) algoritam dodeljuje svakoj instanci broj koji predstavlja verovatnoću pripadanja definisanim klasterima. Uz pomoć ovog algoritma, moguće je odrediti unapred broj klastera ili algoritam odlučuje broj klastera na osnovu kros validacije.  
Klasterovanje se sastoji iz dva koraka:
* na osnovu pretpostavljenih parametara modela za svaku instancu računa se verovatnoća pripadanju klasterima
* na osnovu instanci računaju se ponovo parametri modela    
  
Koraci se ponavljaju dok ima značajne promene loga ukupne verovatnoće modela. Da bi se izbeglo zaglavljivanje u lokalnom maksimumu, primenjuje se postupak više puta uz različit izbor početnih vrednosti parametara.  
  
# 4. Rezultati klasterovanja
Algoritam je na osnovu kros validacije zaključio da slike treba podeliti u 3 klastera.  
![Broj klastera](https://github.com/KsenijaZivkovic/flickr-clusterization/blob/update/screenshots/clusternum.PNG?raw=true)  
Broj instanci po klasteru:  
![Broj instanci po klasteru](https://github.com/KsenijaZivkovic/flickr-clusterization/blob/update/screenshots/clusteredinstances.JPG?raw=true)  
# 5. Vizuelizacija klastera
Izgled aplikacije  
![Aplikacija](https://github.com/KsenijaZivkovic/flickr-clusterization/blob/update/screenshots/app.JPG?raw=true)  
Odabirom klastera iz combo box-a prikazuju se slike koje pripadaju tom klasteru.  
![Klaster1](https://github.com/KsenijaZivkovic/flickr-clusterization/blob/update/screenshots/klaster1.JPG?raw=true)  
![Klaster2](https://github.com/KsenijaZivkovic/flickr-clusterization/blob/update/screenshots/klaster2.JPG?raw=true)  
![Klaster3](https://github.com/KsenijaZivkovic/flickr-clusterization/blob/update/screenshots/klaster3.JPG?raw=true)  
Na osnovu grupisanih slika, imena koja bi mogla biti dodeljena klasterima su:
* Klaster 1 = Mačke u parku
* Klaster 2 = Kacige u obliku mačaka
* Klaster 3 = Slatke mace    

# 6. Tehnička realizacija
Kod je pisan u javi i korišćene su biblioteke: weka i java-json biblioteka.  
Weka je biblioteka koja sadrži algoritme za analizu podataka i predviđanje. Sadrži implementiran algoritam za klasterizaciju EM, koji je korišćen u aplikaciji za grupisanje fotografija.  
Java-json biblioteka koja omogućava parsiranje i generisanje JSON-a. Ova biblioteka je u aplikaciji korišćena za parsiranje odgovora od web servera.

