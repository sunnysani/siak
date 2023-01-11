## URL Staging
https://dev-siakng-api.herokuapp.com/

## Anggota Kelompok
-  1906398414 - Rafli Bangsawan
-  1906285623 - Fadli Aulawi A
-  1906398282 - Alif Iqbal Hazairin
-  1906292931 - Aryaputra Athallah
-  1906293013 - Dewangga Putra Sheradhien
-  1906398723 - Naufal Sani
-  1906285560 - Raden Fausta Anugrah Dianparama

## PBI Sprint 1
-  PBI 1: Pengisian IRS
-  PBI 2: Authentication
-  PBI 3: Pengumuman
-  PBI 4: Pengecekan IRS
-  PBI 14: Ringkasan IRS

## Panduan Instalasi
#### Requirement
-  Java 11
-  Git (sudah terotentikasi)

#### Langkah-Langkah
1. Clone repositori ini ke repositori lokal (misal menggunakan HTTPS)
   ```shell
   git clone https://gitlab.cs.ui.ac.id/ppl-fasilkom-ui/2022/Kelas-A/pasukan-pejuang-lulus/siakng2.0-be.git
   ```

2. Ubah direktori saat ini menjadi direktori repositori lokal (`siakng2.0-be`)
   ```shell
   cd siakng2.0-be
   ```

3. Ubah URL database pada berkas `./src/main/resources/application.properties`. Tambahkan baris berikut
   ```shell
   spring.datasource.url=jdbc:${DATABASE_URL}
   spring.datasource.username=${DATABASE_USERNAME}
   spring.datasource.password=${DATABASE_PASSWORD}
   ```
   di mana `${DATABASE_URL}`, `${DATABASE_USERNAME}`, dan`${DATABASE_PASSWORD}` memiliki value URL database, username dari user yang memiliki akses pada database, dan password-nya sesuai keinginan Anda.
   > Catatan: Anda juga bisa menambahkan environment variable `DATABASE_URL`, `DATABASE_USERNAME`, dan`DATABASE_PASSWORD` 

4. Jalankan
   ```
   ./gradlew bootRun
   ```
