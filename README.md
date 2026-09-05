# CPU Zamanlama Simülatörü

Java Swing kullanılarak geliştirilmiş bir **CPU zamanlama simülasyonu** uygulamasıdır.

Uygulama, farklı CPU zamanlama algoritmalarını aynı işlem verileri üzerinde çalıştırarak sonuçları karşılaştırır. İşlemlerin çalışma sırası ve zaman aralıkları Gantt şeması ile görselleştirilirken, ortalama bekleme süresi, turnaround süresi ve CPU kullanım oranı gibi performans değerleri hesaplanır.

## Özellikler

* 📂 `.txt` dosyasından işlem verisi yükleme
* ✏️ Manuel işlem girişi
* 🎲 Rastgele işlem verisi oluşturma
* ⏱️ Zaman kuantumu belirleme
* 📊 Gantt şeması oluşturma
* 📈 Algoritmaların performans karşılaştırması
* 🧮 Ortalama bekleme süresi hesaplama
* 🧮 Ortalama turnaround süresi hesaplama
* 💻 CPU kullanım oranı hesaplama
* 🤖 Performans sonuçlarına göre algoritma sıralaması
* ⏳ Simülasyon sırasında ilerleme göstergesi

## Kullanılan Algoritmalar

### 1. FCFS (First Come First Served)

İşlemleri geliş zamanlarına göre sırayla çalıştırır.

**Özellikleri:**

* Basit ve anlaşılır bir planlama yöntemidir.
* İşlem sırası geliş zamanına göre belirlenir.
* İşlem başladıktan sonra tamamlanana kadar devam eder.

### 2. SJF (Shortest Job First)

Hazır durumda bulunan işlemler arasından çalışma süresi en kısa olan işlemi seçer.

**Özellikleri:**

* Non-preemptive olarak uygulanmıştır.
* Kısa işlem sürelerine sahip işlemleri önceliklendirir.
* Ortalama bekleme süresini azaltmada avantaj sağlayabilir.

### 3. Round Robin

İşlemleri belirlenen **zaman kuantumu (Quantum)** süresince çalıştırır.

İşlem tamamlanmadığında işlem kuyruğun sonuna eklenir ve sıradaki işleme geçilir.

**Özellikleri:**

* Preemptive bir algoritmadır.
* Zaman kuantumu kullanıcı tarafından belirlenebilir.
* İşlemler arasında daha dengeli bir CPU paylaşımı sağlar.

### 4. Priority Scheduling

İşlemleri öncelik değerlerine göre sıralar.

Bu projede daha düşük öncelik değeri daha yüksek öncelik olarak kabul edilmiştir.

**Özellikleri:**

* Non-preemptive olarak uygulanmıştır.
* İşlemlerin priority değerleri karşılaştırılır.
* Önceliği daha yüksek olan işlem önce çalıştırılır.

## Giriş Yöntemleri

Uygulamada iki farklı veri giriş yöntemi bulunmaktadır.

### Dosyadan Yükleme

`.txt` dosyasından işlem bilgileri okunabilir.

Dosyadaki her satır aşağıdaki formatta olmalıdır:

```text
ProcessID,GelişZamanı,ÇalışmaSüresi,Öncelik
```

Örnek:

```text
P1,0,5,2
P2,1,3,1
P3,2,8,3
P4,3,4,2
```

### Manuel Giriş

Kullanıcı arayüz üzerinden işlemleri tabloya manuel olarak girebilir.

Tabloda aşağıdaki bilgiler kullanılır:

| Alan         | Açıklama                      |
| ------------ | ----------------------------- |
| Process ID   | İşlemin kimliği               |
| Arrival Time | İşlemin sisteme geliş zamanı  |
| Burst Time   | İşlemin CPU'da çalışma süresi |
| Priority     | İşlemin öncelik değeri        |

Ayrıca rastgele işlem verisi oluşturma özelliği de bulunmaktadır.

## Performans Analizi

Her algoritmanın sonucunda aşağıdaki değerler hesaplanır:

### Turnaround Süresi

Bir işlemin sisteme gelişinden tamamlanmasına kadar geçen toplam süredir.

```text
Turnaround Time = Bitiş Zamanı - Geliş Zamanı
```

### Bekleme Süresi

İşlemin hazır kuyruğunda beklediği toplam süredir.

```text
Waiting Time = Turnaround Time - Burst Time
```

### CPU Kullanımı

CPU'nun aktif olarak çalıştığı sürenin toplam süreye oranıdır.

```text
CPU Kullanımı = Toplam Burst Time / Toplam Süre × 100
```

## Gantt Şeması

Simülasyon sonucunda işlemlerin CPU üzerindeki çalışma sırası görsel olarak gösterilir.

Örneğin:

```text
[0]---P1---[5]---P2---[8]---P3---[16]
```

Bu gösterim sayesinde algoritmanın işlemleri hangi sırayla çalıştırdığı ve her işlemin ne kadar süre CPU kullandığı görülebilir.

## Algoritma Karşılaştırması

Simülasyon tamamlandıktan sonra algoritmaların **ortalama bekleme süreleri** karşılaştırılır.

Algoritmalar, ortalama bekleme süresi düşük olandan yüksek olana doğru sıralanır.

Örnek:

```text
=== PERFORMANS KARŞILAŞTIRMASI ===

1. SJF                  : 3.2500 birim
2. Priority             : 4.0000 birim
3. FCFS                 : 5.7500 birim
4. Round                : 6.5000 birim
```

Ayrıca en düşük ortalama bekleme süresine sahip algoritma belirlenerek sonuç bölümünde gösterilir.

## Arayüz

Uygulama Java Swing kullanılarak grafiksel bir kullanıcı arayüzü ile geliştirilmiştir.

Arayüz üç ana sekmeden oluşmaktadır:

### Dosyadan Yükle

* `.txt` dosyası seçme
* Zaman kuantumu belirleme
* Simülasyonu başlatma

### Manuel Giriş

* İşlem ekleme
* İşlem bilgilerini düzenleme
* Rastgele veri oluşturma
* Verileri temizleme
* Zaman kuantumu belirleme
* Simülasyonu çalıştırma

### Analiz ve Grafik

* Algoritma performans karşılaştırması
* Ortalama bekleme süresi sıralaması
* En iyi algoritmanın belirlenmesi
* Gantt şeması / grafik gösterimi

## Kullanılan Teknolojiler

* **Java**
* **Java Swing**
* **AWT**
* **Nesne Yönelimli Programlama**
* **Dosya İşlemleri**
* **Veri Yapıları**
* **CPU Zamanlama Algoritmaları**

## Proje Yapısı

```text
ProcessSchedule/
│
├── ProcessSchedule/
│   ├── ProcessScheduler.java
│   └── ProcessScheduler.jar
│
└── README.md
```

> `.class` dosyaları Java kaynak kodunun derlenmiş çıktılarıdır. Projenin temel kaynak dosyası `ProcessScheduler.java` dosyasıdır.

## Çalıştırma

Bilgisayarınızda Java JDK'nın kurulu olması gerekir.

### Kaynak koddan çalıştırma

Terminali `ProcessScheduler.java` dosyasının bulunduğu klasörde açın.

Derlemek için:

```bash
javac ProcessScheduler.java
```

Çalıştırmak için:

```bash
java ProcessScheduler
```

### JAR dosyasını çalıştırma

Hazır `.jar` dosyasını kullanmak için:

```bash
java -jar ProcessScheduler.jar
```

## Projenin Amacı

Bu proje ile CPU zamanlama algoritmalarının çalışma mantığının uygulamalı olarak incelenmesi amaçlanmıştır.

Aynı işlem kümesinin farklı algoritmalar kullanılarak planlanması ve sonuçların karşılaştırılması sayesinde algoritmalar arasındaki performans farklılıklarının gözlemlenmesi hedeflenmiştir.

Ayrıca Java Swing kullanılarak algoritmaların yalnızca konsol çıktısı yerine grafiksel bir arayüz üzerinden incelenmesi sağlanmıştır.

## Geliştirilebilecek Özellikler

* Yeni CPU zamanlama algoritmalarının eklenmesi
* SRTF gibi preemptive algoritmaların eklenmesi
* Daha ayrıntılı Gantt şeması
* Algoritmaların daha fazla performans kriterine göre karşılaştırılması
* Sonuçların dosyaya aktarılması
* Grafiklerin geliştirilmesi
* Kullanıcı arayüzünün geliştirilmesi

## Geliştirici

**Sude Sena Aydın**
