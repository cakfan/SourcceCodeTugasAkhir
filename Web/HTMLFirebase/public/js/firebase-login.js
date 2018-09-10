var config = {
  apiKey: "AIzaSyCWm1odxYd_fnKyPrTGYGHuHzIRZwuWs9w",
  authDomain: "absensi-a3df8.firebaseapp.com",
};
firebase.initializeApp(config);

firebase.auth().onAuthStateChanged(function(user){
  if(user){
    window.location.replace("/");
  }
});

$('#submit').click(function(){
  var emailnya = $("input[name = 'email']").val();
  var pass = $("input[name = 'password']").val();
  if(emailnya!=""&&pass!=""){
    //console.log("email: "+ emailnya + "\nPassword: "+pass);
    $('.loadingnya').removeClass('sembunyi');
    firebase.auth().signInWithEmailAndPassword(emailnya, pass).catch(function(error) {
        // Handle Errors here.
        var errorCode = error.code;
        var errorMessage = error.message;
        alert(errorCode+"\n"+errorMessage);
        $('.loadingnya').addClass('sembunyi');
    });
  } else {
      alert("Masukkan email dan password.");
  }
});