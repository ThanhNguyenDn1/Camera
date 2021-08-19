// var express=require("express");
// var app=express();
// var server=require("http").createServer(app);
// var io=require("socket.io").listen(server);
// var fs=require("fs");
// server.listen(process.env.PORT||3000);
// console.log("Server running!");
// io.sockets.on('connection', function(socket){
//     console.log("co thiet bi vua ket noi!");
//     socket.on("CLIENT_SEND_IMAGE", function(data){
//         console.log(data);
//         fs.write("images/"+gettime(), data,err=>{
//             if(err) throw err;
//         })
//     });
// })

// function gettime()
// {
//     var date=new date();
//     var milis=date.gettime();
//     return milis+".png";
// }


var express = require("express");
var app=express(); 
var server=require("http").createServer(app);
var io= require("socket.io")(server);
var fs=require("fs");
server.listen(process.env.PORT||3000);
// app.get("/",function(req,res)
// {
//     res.sendFile(__dirname+"/index.html");
// });

console.log("Server Running");


io.sockets.on('connection',function(socket){
    console.log("Co Thiet Bi Vua Ket Noi"); 
    socket.on("CLIENT_SEND_IMAGE",function(data){
        console.log(data);
        fs.writeFile("images/"+getTime(),data,err=>
        {
            if(err) throw err;
            
        });

    })
});

function getTime()
{
    var date=new Date();
    var milis=date.getTime();
    return milis+".png";

}