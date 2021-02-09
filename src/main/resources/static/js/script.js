const toggleSidebar =()=>{
 if($(".sidebar").is(":visible")){
 //true
 $(".sidebar").css("display","none");
 $(".content").css("margin-left","0%");
 }else{
 //false
 $(".sidebar").css("display","block");
 $(".content").css("margin-left","20%");
 }
}

function deleteContact(cid){
swal({
  title: "Are you sure?",
  text: "Once deleted, you will not be able to recover this imaginary file!",
  icon: "warning",
  buttons: true,
  dangerMode: true,
})
.then((willDelete) => {
  if (willDelete) {
   window.location="/user/delete/"+cid;
  } else {
    swal("Your contact is safe!");
  }
});
}
function updateContact(cid){
swal({
  title: "Are you sure?",
  text: "Once updated, you will  be get  a new data for contacts!",
  icon: "warning",
  buttons: true,
  dangerMode: true,
})
.then((willDelete) => {
  if (willDelete) {
   window.location="/user/update/"+cid;
  } else {
    swal("Your contact is safe!");
  }
});
}

const search=()=>{
  let query=$("#search-input").val();
  
  if(query==""){
    $(".search-result").hide();
  }else{
    //search here
    let url=`http://localhost:8099/search/${query}`;
    fetch(url).then((Response)=>{
   return Response.json();
    }).then((data)=>{
   //data
   //console.log(data);
   let text=`<div class='list-group'>`;
   data.forEach((contact) => {
     text+=`<a href='/user/${contact.cid}/contact' class='list-group-item list-group-item-action'>${contact.cname}</a>`
   });

   text+=`</div>`;
   $(".search-result").html(text);
   $(".search-result").show();
    });
    
   
  }

}

//first request to create order
const paymentStart=()=>{
  console.log("payment started");
  let amount=$("#payment_id").val();
  console.log(amount);
  if(amount==null || amount==""){
    swal("Failed!", "Amount required can't be empty!!", "error");
    return;
  }
  //here using ajax to fetch payment :jquery
   $.ajax(
     {
  url:'/user/create_order',
  data:JSON.stringify({amount:amount,info:'order_request'}),
  contentType:'application/json',
  type:'POST',
  dataType:'json',
 
  success:function(response){
    //invoked when success
    console.log(response)
    if(response.status=="created"){
      var options = {
        "key": "rzp_test_XRRWklbEDFgJly", // Enter the Key ID generated from the Dashboard
        "amount": response.amount, // Amount is in currency subunits. Default currency is INR. Hence, 50000 refers to 50000 paise
        "currency": "INR",
        "name": "Smart Contact Manager",
        "description": "Donation",
        "image": "http://localhost:8099/img/payment_logo.jpg",
        "order_id": response.id, //This is a sample Order ID. Pass the `id` obtained in the response of Step 1
        "handler": function (response){
            console.log(response.razorpay_payment_id);
            console.log(response.razorpay_order_id);
            console.log(response.razorpay_signature)
            console.log("payment successfull...!!!");

            updatePaymentOnServer(response.razorpay_payment_id,
              response.razorpay_order_id,
              "paid");
           // alert("woo hoo !! payment successfully done");
           // swal("Good job!", "woo hoo !! payment successfully done!", "success");
        },
        "prefill": {
            "name": " ",
            "email": " ",
            "contact": ""
        },
        "notes": {
            "address": "Razorpay Corporate Office"
        },
        "theme": {
            "color": "#3399cc"
        }
    };

    let rzp = new Razorpay(options);
       rzp.on('payment.failed', function (response){
        console.log(response.error.code);
        consol.log(response.error.description);
        consol.log(response.error.source);
        consol.log(response.error.step);
        consol.log(response.error.reason);
        consol.log(response.error.metadata.order_id);
        consol.log(response.error.metadata.payment_id);
        //alert("oops!!! Payment failed");
        swal("Failed!", "oops!!! Payment failed!", "error");
});
      rzp.open();

    }

  },error:function(error){
  console.log(error)
  console.log("something went wrong")
  }

}

)
};
//
function updatePaymentOnServer(payment_id, order_id, status)
{
$.ajax(
{
  url:'/user/update_order',
  data:JSON.stringify({payment_id:payment_id,
    order_id:order_id,
    status:status}),
  contentType:'application/json',
  type:'POST',
  dataType:'json',
  success:function(response){
    swal("Good job!", "woo hoo !! payment successfully done!", "success");
  },
  error:function(error){
    swal("Failed!", 
    "your Payment is successful,but not updated on server we will contact you or refund initiated within 24 hours",
     "error");
  },
});

}