const clientKey = document.getElementById("clientKey").innerHTML;

// Used to finalize a checkout call in case of redirect
const urlParams = new URLSearchParams(window.location.search);
const sessionId = urlParams.get('sessionId'); // Unique identifier for the payment session
const redirectResult = urlParams.get('redirectResult');

// Typical checkout experience
async function startCheckout() {
  // Type of checkout chosen
  const type = "dropin";
  const regid =  document.getElementById("regid").innerHTML;
  const email =  document.getElementById("email").innerHTML;

  try {
    const checkoutSessionResponse = await callServer("/api/sessions?type=" + type + "&regid=" + regid + "&email=" + email);
    const checkout = await createAdyenCheckout(checkoutSessionResponse);
    checkout.create(type).mount(document.getElementById("payment"));

  } catch (error) {
    console.error(error);
    alert("Error occurred. Look at console for details");
  }
}

// Some payment methods use redirects. This is where we finalize the operation
async function finalizeCheckout() {
  try {
    const checkout = await createAdyenCheckout({id: sessionId});
    checkout.submitDetails({details: {redirectResult}});
  } catch (error) {
    console.error(error);
    alert("Error occurred. Look at console for details");
  }
}

async function createAdyenCheckout(session){
  return new AdyenCheckout(
    {
      clientKey,
      locale: "en_US",
      environment: "test",
      session: session,
      showPayButton: true,
      paymentMethodsConfiguration: {
        ideal: {
          showImage: true,
        },
        card: {
          hasHolderName: true,
          holderNameRequired: true,
          name: "Credit or debit card",
        },
        paypal: {
          environment: "test", // Change this to "live" when you're ready to accept live PayPal payments
          countryCode: "US", // Only needed for test. This will be automatically retrieved when you are in production.
        }
      },
      onPaymentCompleted: (result, component) => {
        console.info("onPaymentCompleted");
        console.info(result, component);
        handleServerResponse(result, component);
      },
      onError: (error, component) => {
        console.error("onError");
        console.error(error.name, error.message, error.stack, component);
        handleServerResponse(error, component);
      },
    }
  );
}

// Calls your server endpoints
async function callServer(url, data) {
  const res = await fetch(url, {
    method: "POST",
    body: data ? JSON.stringify(data) : "",
    headers: {
      "Content-Type": "application/json",
    }
  });

  return await res.json();
}

function handleServerResponse(res, _component) {
  let regid;
  let email;
    switch (res.resultCode) {
      case "Authorised":
        regid =  document.getElementById("regid").innerHTML;
        email =  document.getElementById("email").innerHTML;
        callServer("/payment_success/?regid=" + regid + "&email=" + email);
        location.href = "http://emicrosite.com/";
        break;
      case "Pending":
        location.href = "http://emicrosite.com/";
        break;
      case "Received":
        regid =  document.getElementById("regid").innerHTML;
        email =  document.getElementById("email").innerHTML;
        callServer("/payment_success/?regid=" + regid + "&email=" + email);
        location.href = "http://emicrosite.com/";
        break;
      case "Refused":
        location.href = "http://emicrosite.com/";
        break;
      default:
        location.href = "http://emicrosite.com/";
        break;
    }
}

if (!sessionId) { startCheckout() } else { finalizeCheckout(); }
