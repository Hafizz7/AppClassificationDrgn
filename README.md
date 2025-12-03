# PitayaStem Disease Classification
<body>
  <main class="page">
    <!-- HERO -->
    <section class="hero">
      <div class="hero-inner">
        <div class="hero-left">
          <div class="eyebrow">
            <span class="eyebrow-dot"></span>
            <span>Computer Vision · EfficientNet-B0 · TensorFlow Lite</span>
          </div>
          <h1>
            <span>PitayaStem</span> – Klasifikasi Penyakit Batang Buah Naga
          </h1>
          <p class="hero-subtitle">
            Sistem klasifikasi citra untuk mendeteksi penyakit pada batang tanaman buah naga
            menggunakan arsitektur <strong>EfficientNet-B0</strong>. Model dilatih dari dataset lapangan
            dan dikonversi ke TensorFlow Lite untuk dijalankan langsung di aplikasi Android.
          </p>
          <div class="hero-tags">
            <span class="tag">Image Classification</span>
            <span class="tag">Deep Learning</span>
            <span class="tag">Agriculture · Dragon Fruit</span>
            <span class="tag">Android (Kotlin) · TFLite</span>
          </div>
        </div>
        <div class="hero-right">
          <div class="metric-card">
            <div class="metric-title">Best Model · Transfer Learning (EfficientNet-B0)</div>
            <div class="metric-main">
              <span class="value">98%</span>
              <span class="label">Validation Accuracy</span>
            </div>
            <div class="metric-badges">
              <span class="badge badge-good">Precision &amp; Recall &gt; 95%</span>
              <span class="badge">1.800 images · 4 classes</span>
              <span class="badge">App test accuracy: 96.43%</span>
            </div>
          </div>
        </div>
      </div>
    </section>
    <section>
      <div class="section-header">
        <h2><span class="icon">📊</span> Dataset</h2>
        <small>Collected from dragon fruit orchard · Sanggulan Village, East Kalimantan</small>
      </div>
      <p>
        Dataset berupa citra batang tanaman buah naga yang diambil langsung di kebun menggunakan kamera smartphone
        dengan fokus pada area batang yang menunjukkan gejala penyakit. Total terdapat
        <strong>1.800 citra</strong> yang dibagi ke dalam <strong>4 kelas penyakit</strong>, masing-masing 450 citra.
      </p>
      <div class="grid-2">
        <div class="card-soft">
          <h3>Classes</h3>
          <ul>
            <li><strong>Busuk Batang</strong> (Stem Rot)</li>
            <li><strong>Antraknosa</strong> (Anthracnose)</li>
            <li><strong>Mosaik</strong></li>
            <li><strong>Kudis</strong> (Scab)</li>
          </ul>
          <h3 style="margin-top:8px;">Dataset Split</h3>
          <p>Rasio pembagian data:</p>
          <ul>
            <li><strong>Training:</strong> 80% (360 citra/kelas)</li>
            <li><strong>Validation:</strong> 10% (45 citra/kelas)</li>
            <li><strong>Testing:</strong> 10% (45 citra/kelas)</li>
          </ul>
        </div>
        <div class="card-soft">
          <h3>Preprocessing &amp; Augmentasi</h3>
          <ul>
            <li>Resize citra ke <strong>224 × 224 piksel</strong> (input EfficientNet-B0).</li>
            <li>Normalisasi piksel ke rentang <strong>-1 sampai 1</strong> sesuai standar EfficientNet.</li>
            <li>Augmentasi (hanya pada data training):
              <ul>
                <li>Rotasi</li>
                <li>Zoom</li>
                <li>Horizontal &amp; vertical flip</li>
                <li>Penyesuaian brightness</li>
              </ul>
            </li>
          </ul>
          <p class="quote">
            Preprocessing ini membantu model mengenali tekstur dan pola lesi pada batang
            meskipun dengan variasi sudut pandang dan pencahayaan lapangan.
          </p>
        </div>
      </div>
    </section>
    <section>
      <div class="section-header">
        <h2><span class="icon">🤖</span> Metode &amp; Arsitektur Model</h2>
        <small>EfficientNet-B0 · Multi-class classification (4 diseases)</small>
      </div>
      <div class="grid-2">
        <div class="card-soft">
          <h3>EfficientNet-B0 Architecture</h3>
          <p>
            Model utama yang digunakan adalah <strong>EfficientNet-B0</strong>, arsitektur CNN yang
            menggunakan konsep <em>compound scaling</em> untuk menyeimbangkan kedalaman, lebar, dan resolusi input
            secara efisien.
          </p>
          <ul>
            <li>Backbone: EfficientNet-B0 pretrained ImageNet.</li>
            <li>Lapisan akhir dimodifikasi menjadi:
              <ul>
                <li><code>GlobalAveragePooling2D</code></li>
                <li>Dense + BatchNorm + ReLU + Dropout</li>
                <li>Dense output (4 neuron) + Softmax</li>
              </ul>
            </li>
            <li>Loss function: <strong>Categorical Crossentropy</strong></li>
            <li>Optimizer: <strong>Adam</strong></li>
            <li>Metrics: Accuracy, Precision, Recall, F1-score</li>
          </ul>
        </div>
        <div class="card-soft">
          <h3>Training Scenarios</h3>
          <ul>
            <li><strong>Scenario 1 – Transfer Learning</strong>
              <ul>
                <li>Bobot awal: ImageNet</li>
                <li>Epoch: 40 · batch size: 32</li>
                <li>Training time ~ 2 jam (±180 detik/epoch)</li>
              </ul>
            </li>
            <li><strong>Scenario 2 – Training From Scratch</strong>
              <ul>
                <li>Bobot diinisialisasi dari awal</li>
                <li>Epoch: 80 · batch size: 32</li>
                <li>Training time ~ 7 jam (±315 detik/epoch)</li>
              </ul>
            </li>
          </ul>
          <p class="quote">
            Transfer learning tidak hanya meningkatkan akurasi, tetapi juga mempercepat konvergensi
            dan menstabilkan proses pelatihan.
          </p>
        </div>
      </div>
    </section>
    <section>
      <div class="section-header">
        <h2><span class="icon">📈</span> Hasil &amp; Evaluasi Model</h2>
        <small>Perbandingan transfer learning vs training dari awal</small>
      </div>
      <div class="grid-2">
        <div class="card-soft">
          <h3>Ringkasan Performa</h3>
          <table class="metrics-table">
            <tr>
              <th>Aspek</th>
              <th>Transfer Learning</th>
              <th>Tanpa Transfer</th>
            </tr>
            <tr>
              <td>Best Training Acc.</td>
              <td>98.59%</td>
              <td>88.05%</td>
            </tr>
            <tr>
              <td>Best Validation Acc.</td>
              <td class="metric">98.89%</td>
              <td>87.22%</td>
            </tr>
            <tr>
              <td>Total Epoch</td>
              <td>40</td>
              <td>80</td>
            </tr>
            <tr>
              <td>Total Training Time</td>
              <td>± 2 jam</td>
              <td>± 7 jam</td>
            </tr>
          </table>
          <p class="quote">
            Model EfficientNet-B0 dengan transfer learning menghasilkan akurasi hingga
            <strong>98%</strong> dengan precision dan recall rata-rata di atas 95% untuk keempat kelas penyakit.
          </p>
        </div>
        <div class="card-soft">
          <h3>Performa Per Kelas (Transfer Learning)</h3>
          <table class="metrics-table">
            <tr>
              <th>Kelas</th>
              <th>Accuracy</th>
              <th>Precision</th>
              <th>Recall</th>
              <th>F1</th>
            </tr>
            <tr>
              <td>Antraknosa</td>
              <td>100</td><td>96</td><td>98</td><td>97</td>
            </tr>
            <tr>
              <td>Busuk Batang</td>
              <td>100</td><td>100</td><td>98</td><td>99</td>
            </tr>
            <tr>
              <td>Kudis</td>
              <td>98</td><td>96</td><td>98</td><td>97</td>
            </tr>
            <tr>
              <td>Mosaik</td>
              <td>98</td><td>100</td><td>98</td><td>99</td>
            </tr>
          </table>
          <p class="quote">
            Pendekatan tanpa transfer learning hanya mencapai akurasi 84% dengan recall terendah
            47% pada kelas Kudis, menunjukkan kesulitan model belajar dari awal pada dataset terbatas.
          </p>
        </div>
      </div>
    </section>
    <section>
      <div class="section-header">
        <h2><span class="icon">📱</span> Integrasi ke Aplikasi Android</h2>
        <small>PitayaStemVision · Offline classification using TensorFlow Lite</small>
      </div>
      <p>
        Model terbaik kemudian dikonversi ke format <strong>TensorFlow Lite (.tflite)</strong> dan diintegrasikan
        ke dalam aplikasi Android bernama <strong>PitayaStemVision</strong>. Aplikasi dibangun dengan bahasa
        <strong>Kotlin</strong> dan memanfaatkan library TensorFlow Lite Android sehingga proses inferensi dapat
        dilakukan secara lokal tanpa koneksi internet.
      </p>
      <div class="grid-2">
        <div class="card-soft">
          <h3>Fitur Utama Aplikasi</h3>
          <ul>
            <li>Pengambilan citra batang dari <strong>kamera</strong> atau <strong>galeri</strong>.</li>
            <li>Proses <strong>klasifikasi penyakit</strong> secara otomatis di perangkat.</li>
            <li>Halaman hasil dengan informasi:
              <ul>
                <li>Nama penyakit</li>
                <li>Gejala &amp; penyebab</li>
                <li>Saran penanganan</li>
              </ul>
            </li>
            <li>Fitur <strong>riwayat klasifikasi</strong> dengan opsi penghapusan.</li>
          </ul>
        </div>
        <div class="card-soft">
          <h3>Pengujian Aplikasi</h3>
          <ul>
            <li>Pengujian <strong>black box</strong> pada 7 jenis smartphone berbeda.</li>
            <li>Seluruh skenario uji (kamera, galeri, cek hasil, riwayat, hapus riwayat) berstatus
              <strong>Sukses</strong>.</li>
            <li>Uji data eksternal (2 citra per kelas) dari lokasi berbeda menghasilkan
              <strong>akurasi rata-rata 96.43%</strong> pada aplikasi.</li>
          </ul>
          <p class="quote">
            Hasil ini menunjukkan bahwa model yang dikonversi ke TFLite tetap stabil dan akurat
            ketika dijalankan di lingkungan perangkat mobile sungguhan.
          </p>
        </div>
      </div>
    </section>
    <section>
      <div class="section-header">
        <h2><span class="icon">🚀</span> Pengembangan Lanjutan</h2>
        <small>Research roadmap</small>
      </div>
      <ul>
        <li>Memperluas dataset dengan variasi lokasi, pencahayaan, dan perangkat kamera yang berbeda.</li>
        <li>Menerapkan teknik <strong>k-fold cross validation</strong> untuk meningkatkan generalisasi model.</li>
        <li>Mengembangkan metode yang mampu:
          <ul>
            <li>Mendeteksi lebih dari satu penyakit dalam satu citra (multi-label).</li>
            <li>Menandai posisi lesi penyakit pada batang (localization / segmentation).</li>
          </ul>
        </li>
        <li>Menambahkan dashboard web / API untuk monitoring dan integrasi sistem pertanian cerdas.</li>
      </ul>
    </section>
    <p class="footer">
      PitayaStem Disease Classification · EfficientNet-B0 · TensorFlow Lite · Android<br />
    </p>
  </main>
</body>
</html>
