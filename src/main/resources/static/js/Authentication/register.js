let arr = [true,true,true]

async function checkAvailability(id) {

    let signupSubmitbutton = document.getElementById("signup-submit-button")

    let response;
    try {
        if (id === 'username') {
            const username = document.getElementById("username").value;
            response = await fetch(`/checkAvailability?userId=${username}&type=username`);

            if (response.ok) {
                const isUserFound = await response.json();
                const errorDiv = document.getElementById("username-error");


                if (isUserFound) {
                    arr[0]=false
                    errorDiv.textContent = username + " Not available";
                    errorDiv.style.color = "#e74c3c";
                } else {
                    arr[0]=true
                    errorDiv.textContent = "";
                }
                updateButton()
            } else {
                console.error("Error!");
            }

        } else if (id === 'emailId') {
            const emailId = document.getElementById("email").value;

            response = await fetch(`/checkAvailability?userId=${emailId}&type=email`);

            if (response.ok) {

                const isUserFound = await response.json();

                const errorDiv = document.getElementById("email-error");


                if (isUserFound) {
                    arr[1]=false
                    errorDiv.textContent = emailId + " Not available";
                    errorDiv.style.color = "#e74c3c";

                } else {
                    arr[1]=true
                    errorDiv.textContent = "";
                }
                updateButton()
            } else {
                console.error("Error!");
            }

        } else if (id === 'phoneNumber') {
            const phoneNumber = document.getElementById("phone").value
            response = await fetch(`/checkAvailability?userId=${phoneNumber}&type=mobile`);

            if (response.ok) {
                const isUserFound = await response.json();
                const errorDiv = document.getElementById("phoneNumber-error");


                if (isUserFound) {
                    arr[2]=false
                    errorDiv.textContent = phoneNumber + " Not available";
                    errorDiv.style.color = "#e74c3c";
                } else {
                    arr[2]=true
                    errorDiv.textContent = "";

                }
            } else {
                console.error("Error!");
            }
        }
    } catch (e) {
        console.error("Error!" + e.toString());
    }
}

function updateButton(){
    let signupSubmitbutton = document.getElementById("signup-submit-button")
    if(arr[0] && arr[1] && arr[2]){
        signupSubmitbutton.disabled = false;
        signupSubmitbutton.style.cursor = "pointer"
    }else{
        signupSubmitbutton.disabled = true;
        signupSubmitbutton.style.cursor = "not-allowed"
    }
}