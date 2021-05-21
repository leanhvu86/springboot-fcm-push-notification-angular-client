importScripts('https://www.gstatic.com/firebasejs/7.6.0/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/7.6.0/firebase-messaging.js');

firebase.initializeApp({
    apiKey: "AIzaSyAdn_uKvhinZ19DR7Gv_n33b-t0Q_bzoe8",
    authDomain: "share-van-manager.firebaseapp.com",
    databaseURL: "https://share-van-manager.firebaseio.com",
    projectId: "share-van-manager",
    storageBucket: "share-van-manager.appspot.com",
    messagingSenderId: "770488551388",
    appId: "1:770488551388:web:b12dbcf18aec4602e17119",
    measurementId: "G-4M96Y8GHGQ"
});

const messaging = firebase.messaging();