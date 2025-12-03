<DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>PitayaStem Disease Classification – EfficientNet-B0</title>
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet" />
  <style>
    :root {
      --bg: #020617;
      --bg-soft: #020617;
      --bg-card: #0f172a;
      --accent: #22c55e;
      --accent2: #38bdf8;
      --accent-soft: rgba(34, 197, 94, 0.1);
      --text-main: #e5e7eb;
      --text-soft: #9ca3af;
      --border-subtle: rgba(148, 163, 184, 0.4);
      --shadow-soft: 0 20px 40px rgba(15, 23, 42, 0.85);
    }

    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
    }

    body {
      font-family: "Inter", system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
      background:
        radial-gradient(circle at top, #0b1120 0, #020617 40%, #000 100%);
      color: var(--text-main);
      line-height: 1.6;
      -webkit-font-smoothing: antialiased;
    }

    .page {
      max-width: 1120px;
      margin: 32px auto 40px;
      padding: 0 16px 40px;
    }

    /* HERO */
    .hero {
      border-radius: 26px;
      padding: 28px 26px 24px;
      border: 1px solid var(--border-subtle);
      background:
        radial-gradient(circle at 0% 0%, rgba(34, 197, 94, 0.16) 0, transparent 50%),
        radial-gradient(circle at 100% 0%, rgba(56, 189, 248, 0.18) 0, transparent 55%),
        linear-gradient(140deg, #020617 0, #020617 55%, #020617 100%);
      position: relative;
      overflow: hidden;
      box-shadow: var(--shadow-soft);
    }

    .hero-inner {
      position: relative;
      z-index: 1;
      display: flex;
      flex-wrap: wrap;
      gap: 24px;
      align-items: center;
      justify-content: space-between;
    }

    .hero-left {
      max-width: 620px;
    }

    .eyebrow {
      display: inline-flex;
      align-items: center;
      gap: 8px;
      padding: 4px 10px;
      border-radius: 999px;
      border: 1px solid rgba(148, 163, 184, 0.6);
      background: rgba(15, 23, 42, 0.9);
      color: var(--text-soft);
      font-size: 11px;
      text-transform: uppercase;
      letter-spacing: 0.14em;
    }

    .eyebrow-dot {
      width: 7px;
      height: 7px;
      border-radius: 999px;
      background: var(--accent);
      box-shadow: 0 0 0 6px rgba(34, 197, 94, 0.2);
    }

    h1 {
      margin-top: 14px;
      font-size: clamp(28px, 4vw, 36px);
      letter-spacing: -0.03em;
      font-weight: 700;
    }

    h1 span {
      background: linear-gradient(120deg, #22c55e, #38bdf8);
      -webkit-background-clip: text;
      color: transparent;
    }

    .hero-subtitle {
      margin-top: 10px;
      color: var(--text-soft);
      font-size: 14px;
      max-width: 540px;
    }

    .hero-tags {
      margin-top: 16px;
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
    }

    .tag {
      padding: 5px 10px;
      border-radius: 999px;
      border: 1px solid rgba(148, 163, 184, 0.5);
      background: rgba(15, 23, 42, 0.9);
      font-size: 11px;
      color: var(--text-soft);
    }

    .hero-right {
      flex: 1 0 220px;
      max-width: 260px;
    }

    .metric-card {
      background: rgba(15, 23, 42, 0.98);
      border-radius: 20px;
      padding: 16px 16px 12px;
      border: 1px solid rgba(148, 163, 184, 0.65);
      backdrop-filter: blur(14px);
      box-shadow: 0 20px 40px rgba(0, 0, 0, 0.8);
    }

    .metric-title {
      font-size: 11px;
      text-transform: uppercase;
      letter-spacing: 0.14em;
      color: var(--text-soft);
      margin-bottom: 6px;
    }

    .metric-main {
      display: flex;
      align-items: baseline;
      gap: 6px;
    }

    .metric-main span.value {
      font-size: 28px;
      font-weight: 700;
    }

    .metric-main span.label {
      font-size: 12px;
      color: var(--text-soft);
    }

    .metric-badges {
      margin-top: 10px;
      display: flex;
      flex-wrap: wrap;
      gap: 6px;
      font-size: 11px;
      color: var(--text-soft);
    }

    .badge {
      padding: 3px 7px;
      border-radius: 999px;
      background: rgba(15, 23, 42, 0.9);
      border: 1px solid rgba(148, 163, 184, 0.6);
    }

    .badge-good {
      color: var(--accent);
      border-color: rgba(34, 197, 94, 0.7);
      background: rgba(22, 163, 74, 0.13);
    }

    /* SECTIONS */
    section {
      margin-top: 26px;
      background: rgba(15, 23, 42, 0.96);
      border-radius: 22px;
      padding: 20px 18px 18px;
      border: 1px solid rgba(30, 64, 175, 0.7);
      box-shadow: var(--shadow-soft);
    }

    section + section {
      margin-top: 18px;
    }

    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: baseline;
      gap: 12px;
      margin-bottom: 10px;
    }

    .section-header h2 {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 17px;
      font-weight: 600;
    }

    .section-header h2 span.icon {
      font-size: 18px;
    }

    .section-header small {
      font-size: 11px;
      color: var(--text-soft);
    }

    p {
      font-size: 14px;
      color: var(--text-soft);
      margin-bottom: 8px;
    }

    ul {
      margin: 4px 0 8px 16px;
      color: var(--text-soft);
      font-size: 14px;
    }

    ul li {
      margin-bottom: 4px;
    }

    .grid-2 {
      display: grid;
      grid-template-columns: minmax(0, 1.15fr) minmax(0, 1fr);
      gap: 16px;
      margin-top: 8px;
    }

    .card-soft {
      background: rgba(15, 23, 42, 0.9);
      border-radius: 16px;
      padding: 11px 12px 10px;
      border: 1px solid rgba(148, 163, 184, 0.45);
      font-size: 13px;
    }

    .card-soft h3 {
      font-size: 13px;
      margin-bottom: 4px;
      font-weight: 600;
    }

    .pill-list {
      display: flex;
      flex-wrap: wrap;
      gap: 6px;
      margin-top: 6px;
    }

    .pill {
      font-size: 11px;
      padding: 4px 8px;
      border-radius: 999px;
      background: rgba(15, 23, 42, 0.9);
      border: 1px solid rgba(148, 163, 184, 0.45);
      color: var(--text-soft);
    }

    .metrics-table {
      width: 100%;
      border-collapse: collapse;
      font-size: 13px;
      margin-top: 6px;
    }

    .metrics-table th,
    .metrics-table td {
      padding: 6px 7px;
      text-align: left;
      border-bottom: 1px solid rgba(30, 64, 175, 0.7);
    }

    .metrics-table th {
      font-weight: 500;
      color: var(--text-soft);
      background: rgba(15, 23, 42, 0.9);
    }

    .metrics-table td.metric {
      font-weight: 600;
    }

    .quote {
      font-size: 13px;
      color: var(--text-soft);
      border-left: 2px solid var(--accent2);
      padding-left: 10px;
      margin-top: 6px;
    }

    pre {
      font-family: "JetBrains Mono", ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;
      font-size: 12px;
      background: rgba(15, 23, 42, 0.97);
      padding: 10px;
      border-radius: 10px;
      overflow-x: auto;
      border: 1px solid rgba(30, 64, 175, 0.8);
      color: #e5e7eb;
    }

    code {
      font-family: inherit;
      font-size: 12px;
    }

    .footer {
      text-align: center;
      font-size: 11px;
      color: var(--text-soft);
      margin-top: 20px;
      opacity: 0.7;
    }

    @media (max-width: 768px) {
      .hero-inner {
        flex-direction: column;
        align-items: flex-start;
      }
      .hero-right {
        max-width: none;
        width: 100%;
      }
      .grid-2 {
        grid-template-columns: minmax(0, 1fr);
      }
      .page {
        margin-top: 20px;
      }
    }
  </style>
</head>
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
