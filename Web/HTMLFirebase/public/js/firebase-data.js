

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
        var rfid_siswa = datanya.rfid_siswa;
        var tipe_akun = datanya.tipe_akun;
        //console.log(tipe_akun);
        if(tipe_akun == "admin"){
          console.log("Kamu admin!");
          $('table.absen').addClass('sembunyi');
          var rfid_list = firebase.database().ref("rfid_list");
          rfid_list.on('value', function(snapshot){
            var list = snapshot.val();
            var lrfid = Object.keys(list);
            for(var i=0;i<lrfid.length;i++){
              var rfid_info = firebase.database().ref("rfid_list/"+lrfid[i]);
              rfid_info.on('value', function(snapshot){
                var rfidin = snapshot.val();
                var status = rfidin.status;
                if(status==1){
                  var nama = rfidin.nama_siswa;
                  var jumlah = firebase.database().ref("jumlah");
                    jumlah.on('value', function(snapshot){
                      var jml = snapshot.val();
                      $('.paste-hadir').append($('<p>'+nama+'</p>'));
                      $('.jumlah-absen').text(jml);
                      console.log("lenght: "+lrfid.length);
                      
                    })

                }
                //console.log("UID: "+ruid + "\n Status: "+ status);
              })
              
            }
            
          });

        } else{
            var dbrfid = firebase.database().ref("absensi/"+rfid_siswa);
            dbrfid.on('value', function(snapshot){
            $('.loadingnya,.admin-absen').addClass('sembunyi');
            var absenya = snapshot.val();
            var senin = absenya.Senin;
            var selasa = absenya.Selasa;
            var rabu = absenya.Rabu;
            var kamis = absenya.Kamis;
            var jumat = absenya.Jumat;
            var sabtu = absenya.Sabtu;
            var tak = "Tidak ada data.";
            console.log(senin+"\n"+selasa+"\n"+rabu+"\n"+kamis+"\n"+jumat+"\n"+sabtu);
            if(senin != ""){
              $('.senin').text(senin);
            } else{
              $('.senin').text(tak);
            }
            if(selasa != ""){
              $('.selasa').text(selasa);
            } else{
              $('.selasa').text(tak);
            }
            if(rabu != ""){
              $('.rabu').text(rabu);
            } else{
              $('.rabu').text(tak);
            }
            if(kamis != ""){
              $('.kamis').text(kamis);
            } else{
              $('.kamis').text(tak);
            }
            if(jumat != ""){
              $('.jumat').text(jumat);
            } else{
              $('.jumat').text(tak);
            }
            if(sabtu != ""){
              $('.sabtu').text(sabtu);
            } else{
              $('.sabtu').text(tak);
            }
            
          });

        }
        
        // $('.nama').text(" "+nama);
        // $('.alamat').text(alamat);
        // $('.nama_siswa').text(nama_siswa);
        // $('.rfid_siswa').text(rfid_siswa);
        // $('.absensi').text(absensi);
        //console.log(snapshot.val());
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