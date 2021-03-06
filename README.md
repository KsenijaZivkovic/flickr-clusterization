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
![JSON odgovor](https://github.com/KsenijaZivkovic/flickr-clusterization/blob/master/screenshots/jsonodgovor.JPG?raw=true)  
Prikupljeni podaci čuvaju se u fajlu [data/slike.arff](https://github.com/KsenijaZivkovic/flickr-clusterization/blob/master/data/slike.arff).  
Atributi koji se čuvaju su: id, link ka slici, naziv slike, tagovi i broj klastera kome slika pripada, koji je inicijalno nepoznat i vrednost mu se dodeljuje nakon izvršene klasterizacije.  
![arff fajl](https://github.com/KsenijaZivkovic/flickr-clusterization/blob/master/screenshots/arff.JPG?raw=true)  
  
# 3. EM klasterovanje
Klasterovanje je određivanje pripadnosti objekata različitim grupama, tako da u svakoj grupi budu objekti koji dele neke zajedničke osobine. Expectation Minimization (EM) algoritam dodeljuje svakoj instanci broj koji predstavlja verovatnoću pripadanja definisanim klasterima. Uz pomoć ovog algoritma, moguće je odrediti unapred broj klastera ili algoritam odlučuje broj klastera na osnovu kros validacije.  
Klasterovanje se sastoji iz dva koraka:
* na osnovu pretpostavljenih parametara modela za svaku instancu računa se verovatnoća pripadanju klasterima
* na osnovu instanci računaju se ponovo parametri modela    
  
Koraci se ponavljaju dok ima značajne promene loga ukupne verovatnoće modela. Da bi se izbeglo zaglavljivanje u lokalnom maksimumu, primenjuje se postupak više puta uz različit izbor početnih vrednosti parametara.  
  
Klasterizacija je vršena na osnovu sledećih atributa: title i tags. Da bi atributi bili pogodni za klasterovanje, korišćen je StringToWordVector filter, koji pretvara atribute tipa String u set atributa koji predstavljaju reči koje su se pojavile u tom atributu. Ostali atributi su ignorisani.  
  
# 4. Rezultati klasterovanja
Algoritam je na osnovu kros validacije zaključio da slike treba podeliti u 3 klastera.  
![Broj klastera](https://github.com/KsenijaZivkovic/flickr-clusterization/blob/master/screenshots/clusternum.PNG?raw=true)  
Broj instanci po klasteru:  
![Broj instanci po klasteru](https://github.com/KsenijaZivkovic/flickr-clusterization/blob/master/screenshots/clusteredinstances.JPG?raw=true)  
# 5. Vizuelizacija klastera
Izgled aplikacije  
![Aplikacija](https://github.com/KsenijaZivkovic/flickr-clusterization/blob/master/screenshots/app.JPG?raw=true)  
Odabirom klastera iz combo box-a prikazuju se slike koje pripadaju tom klasteru.  
![Klaster1](https://github.com/KsenijaZivkovic/flickr-clusterization/blob/master/screenshots/klaster1.JPG?raw=true)  
![Klaster2](https://github.com/KsenijaZivkovic/flickr-clusterization/blob/master/screenshots/klaster2.JPG?raw=true)  
![Klaster3](https://github.com/KsenijaZivkovic/flickr-clusterization/blob/master/screenshots/klaster3.JPG?raw=true)  
Na osnovu grupisanih slika, imena koja bi mogla biti dodeljena klasterima su:
* Klaster 1 = Mačke u parku
* Klaster 2 = Kacige u obliku mačaka
* Klaster 3 = Slatke mace    

# 6. Tehnička realizacija
Kod je pisan u javi i korišćene su biblioteke: [weka](http://www.cs.waikato.ac.nz/ml/weka/) i [java-json](http://www.oracle.com/technetwork/articles/java/json-1973242.html) biblioteka.  
Weka je biblioteka koja sadrži algoritme za analizu podataka i predviđanje. Sadrži implementiran algoritam za klasterizaciju EM, koji je korišćen u aplikaciji za grupisanje fotografija.  
Java-json biblioteka koja omogućava parsiranje i generisanje JSON-a. Ova biblioteka je u aplikaciji korišćena za parsiranje odgovora od web servera.  

# 7. Priznanja  
Aplikacija je razvijena kao deo projekta u okviru predmeta [Inteligentni sistemi](http://ai.fon.bg.ac.rs/osnovne/inteligentni-sistemi/) na Fakultetu organizacionih nauka u Beogradu.

# 8. Licenca  
Ovaj projekat je publikovan pod MIT licencom.

The MIT License (MIT)

Copyright (c) 2016 Ksenija Zivkovic - xenija.zivkovic@gmail.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
