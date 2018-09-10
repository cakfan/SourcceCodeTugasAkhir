

var config = {
    apiKey: "AIzaSyCWm1odxYd_fnKyPrTGYGHuHzIRZwuWs9w",
    authDomain: "absensi-a3df8.firebaseapp.com",
    projectId: "absensi-a3df8",
    databaseURL: "https://absensi-a3df8.firebaseio.com",
    messagingSenderId: "98686068394",
  };
  firebase.initializeApp(config);
  $('.loadingnya').removeClass('sembunyi');
  
    firebase.auth().onAuthStateChanged(function(user){
      if(!user){
        window.location.replace("login.html");
      } else{
        var user_id = user.uid;
        var db = firebase.database();
        var data = db.ref("users/"+user_id);
        data.on('value', function(snapshot){
          $('.loadingnya').addClass('sembunyi');
          var datanya = snapshot.val();
          var nama = datanya.nama;
          var alamat = datanya.alamat;
          var nama_siswa = datanya.nama_siswa;
          var rfid_siswa = datanya.rfid_siswa;
          var absensi = datanya.absensi;
          var gambar = datanya.gambar;
          $('.imgProfile').attr('src',gambar);
          $('.namaSiswa').text(nama_siswa);
          $('.rfidnya').text(rfid_siswa);
          $('.namaWali').text(nama);
          $('.alamatnya').text(alamat);
          //console.log(gambar);
        }, function (error) {
          console.log("Error: " + error.code);
        })
  
      }
    });
    $('#keluar').click(function(){
      firebase.auth().signOut().then(function() {
        // Sign-out successful.
      }).catch(function(error) {
        alert(error);
      });
    });