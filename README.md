# Klasterizacija i pretraživanje slika sa Flickr-a
Korišćenjem Flickr API-ja aplikacija preuzima podatke o slikama, klasterizuje ih na osnovu naziva i tagova slika i prikazuje slike po pronađenim klasterima.
#DataSet
Podaci nad kojima se radi klasterizacija prikupljeni su sa [Flickr](http://www.flickr.com)-a.
Za prikupljanje podataka korišćene su sledeće metode:
* **flickr.photos.search**, koja vraća listu slika koje zadovoljavaju kriterijum pretrage.
Korišćeni parametri metode:
 * api_key, ključ aplikacije
 * text, tekst pretrage
 * per_page, broj rezultata po strani
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
  
Prikupljeni podaci čuvaju se u fajlu data/slike.arff.
