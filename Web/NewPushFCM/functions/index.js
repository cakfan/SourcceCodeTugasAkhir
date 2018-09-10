'use-strict';
const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);

var db = admin.database();

exports.kirimNotifikasi = functions.database.ref("baca_rfid").onWrite(event => {
    db.ref("baca_rfid").once("value", function(snapshot){
        var baca = snapshot.val();
        if(baca != 0){
            db.ref("notifikasi").on("value", function(snapshot){
                var rfid = snapshot.val();
                db.ref("rfid_list/" + rfid + "/userid").on("value", function(snapshot){
                    var userid = snapshot.val();
                    db.ref("users/"+userid+"/token_id").on("value", function(snapshot){
                        var tokennya = snapshot.val();
                        db.ref("users/"+userid+"/nama_siswa").on("value", function(snapshot){
                            var siswa = snapshot.val();
                            db.ref("rfid_list/"+rfid+"/status").on("value", function(snapshot){
                                var stts = snapshot.val();
                                var status;
                                if(stts == 1){
                                    status = " hari ini sudah absen.";
                                    return Promise.all([tokennya,siswa]).then(result => {
                                        var token = result[0];
                                        var nama = result[1];
                                        var pesan = nama + status;
                                        const payload = {
                                            notification: {
                                                title: "Absensi App",
                                                body: pesan,
                                                icon: "default",
                                                sound:"default",
                                                click_action: "com.cakfan.absensiapp.MainActivity.TARGETNOTIFICATION"
                                            },
                                            data: {
                                                pesannya: pesan
                                            }
                                        };
                                        return admin.messaging().sendToDevice(token, payload).then(result => {
                                            console.log("Notifikasi terkirim.");
                                        });
                                        //console.log("Token: "+token+" Siswa: "+nama);
                                    });
                                }
                                if(stts == 0){
                                    status = " hari ini sudah pulang.";
                                    return Promise.all([tokennya,siswa]).then(result => {
                                        var token = result[0];
                                        var nama = result[1];
                                        var pesan = nama + status;
                                        const payload = {
                                            notification: {
                                                title: "Absensi App",
                                                body: pesan,
                                                icon: "default",
                                                sound:"default",
                                                click_action: "com.cakfan.absensiapp.MainActivity.TARGETNOTIFICATION"
                                            },
                                            data: {
                                                pesannya: pesan
                                            }
                                        };
                                        return admin.messaging().sendToDevice(token, payload).then(result => {
                                            console.log("Notifikasi terkirim.");
                                        });
                                        //console.log("Token: "+token+" Siswa: "+nama);
                                    });
                                }
                            });
                            

                        });                        

                    });
                    
                });
                
            });
            
        } else{
            console.log("Absen kosong");
        }

    });
    
})