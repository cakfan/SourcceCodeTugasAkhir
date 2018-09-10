'use-strict';
const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);

var db = admin.database();

exports.kirimNotifikasi = functions.database.ref("DETECTED MOTION").onUpdate(event => {
    db.ref("DETECTED MOTION").once("value", function(snapshot){
        var baca = snapshot.val();
        if(baca != 0){
            db.ref("token").on("value", function(snapshot){
                var token = snapshot.val();
                var ppp = "Ada gerakan coy!";
                return Promise.all([token,ppp]).then(result => {
                                var token = result[0];
                                var pesan = result[1];
                                
                                const payload = {
                                    notification: {
                                        title: "Absensi App",
                                        body: pesan,
                                        icon: "default",
                                        sound:"default"
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

            });
            
        } else{
            console.log("Absen kosong");
        }

    });
    
})