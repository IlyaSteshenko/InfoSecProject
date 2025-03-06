import Patent from "./patent.js";
import Path from "./path.js";
import Database from "./database.js";


function notifyUser(string) {
    function fade(notification){
        const FADE_TIME = 500;
        notification.classList.add("notificationHidden");
        setTimeout(()=>{notification.remove();}, FADE_TIME);
    }
    const EXPIRY_TIME= 5000;
    let container = document.getElementsByClassName("notificationList")[0];
    if (container === undefined) {

        container = document.createElement("div");
        container.classList.add("notificationList");
        document.body.prepend(container);
    }
    let notification = document.createElement("div");
    notification.classList.add("notificationHidden", "notification");
    notification.textContent = string;
    container.prepend(notification);
    setTimeout(()=>notification.classList.remove("notificationHidden"), 10);
    setTimeout(fade,EXPIRY_TIME,notification);
    notification.addEventListener("click", ()=>{fade(notification)})
}

function createOnClickID(element, id) {
    return ()=>Path.initWindow(`Патент#${id}`);
}







// document.querySelectorAll(".patentHeader").forEach(
//     function (element, number, ignored) {
//         element.addEventListener("click", createOnClickID(element));
//     }
// ); код из начала создания
function initCreatePatentButton() {
    document.querySelectorAll(".create").forEach((elem) => {
        elem.addEventListener("click", () => {
            let patent = new Patent("Гойда", 123, "Охлобыстин",
                "2.1.2020", "Священная война")
                .generateElement(createOnClickID);
            document.querySelector(".patentsList").prepend(patent);
            Path.next("Гойда", () => {
                notifyUser("Гойда назад, перемога перемога");
                patent.remove();
            })
        });
    })

}

function DisplayRecentPatents(){
    let patentListElement = document.querySelector(".patentsList")
    if (patentListElement === null) {
        console.log("DisplayRecentPatents не нашел patentsList на странице, я не хочу добавлять проверки, иди чини")
        return;
    }
    patentListElement.replaceChildren();
    let queryResult = Database.fetchPatentsList();
    queryResult.forEach((patent)=>patentListElement.prepend(patent.generateElement(createOnClickID)));

}
window.addEventListener("load", ()=>{
    DisplayRecentPatents();
    initCreatePatentButton();
})
