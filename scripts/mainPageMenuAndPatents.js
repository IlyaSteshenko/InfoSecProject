const settings = document.querySelector('.settings');
const image = document.querySelector('.settings>img');
const header = document.querySelector('.head');

const AMenu = document.querySelector(".accordionMenu");
const SideMenu = document.querySelector(".sideMenu");
const Controls = document.querySelector(".controls");
const ControlsContent = document.querySelectorAll(".controls>a, .controls>.search-container");
const ExcludeReplacing = document.querySelectorAll(".settings")
const SMenuContent = document.querySelectorAll(".sideMenu>*");
const switchMenuFunc = () => {
    if (window.innerWidth < 1024 && mode !== "compact") {
        AMenu.replaceChildren(...ControlsContent, ...SMenuContent);
        mode = "compact";
    } else if (window.innerWidth >= 1024 && mode !== "full") {
        SideMenu.replaceChildren(...SMenuContent);
        Controls.replaceChildren(...ControlsContent, ...ExcludeReplacing)
        mode = "full";
    }
}
let mode = "full"; // "compact"
switchMenuFunc();
window.addEventListener("resize", switchMenuFunc, passive = true);

function animCloseAMenu() {
    AMenu.style.removeProperty("height");
    AMenu.style.removeProperty('border-top-width')
    setTimeout(() => {
        AMenu.classList.remove('visible')

    }, 500)
}

function animOpenAMenu() {
    AMenu.classList.add('visible');

    AMenu.style.height = AMenu.scrollHeight.toString() + "px";
    AMenu.style.borderTopWidth = "3px";
}

function radiancePointer(bgObject) {
    window.addEventListener("mousemove", (event) => {

        let coords = bgObject.getClientRects().item(0);
        if(
            (coords === null) ||
            (coords.x - event.x > 400) ||
            (coords.y - event.y > 400) ||
            (coords.x + coords.height - event.x < -400) ||
            (coords.y + coords.width - event.y < -400)
        ){
            if (bgObject.style.hasOwnProperty("background-position")){
                bgObject.style.removeProperty("background-position")
            }
            return;
        }
        let x = coords.x + 300;
        let y = coords.y + 300;
        let cx = event.x;
        let cy = event.y;
        bgObject.style.backgroundPosition = `${cx - x}px ${cy - y}px, 0 0`
    }, passive = true)
}

document.querySelectorAll(".patent").entries().forEach((each) => radiancePointer(each[1]));
document.querySelectorAll(".update").entries().forEach((each) => radiancePointer(each[1]));

image.addEventListener('click', () => {
    if (image.classList.contains('settings-rotated')) {
        image.classList.remove('settings-rotated');
        // Если клик по актив
        settings.classList.remove('visible');
        animCloseAMenu();
        //AMenu.classList.remove('visible');

    } else {
        settings.classList.add('visible');
        // Если клик по пассив
        animOpenAMenu();
        image.classList.add('settings-rotated');
    }
}, true);

function focusoutHandler(event){
    if (settings.contains(event.relatedTarget)) return;
    if(AMenu.contains(event.relatedTarget)) return;
    if (image.classList.contains('settings-rotated')) {
        image.classList.remove('settings-rotated');
        settings.classList.remove('visible');
        animCloseAMenu();
    }
}
header.addEventListener('focusout', (event) => {focusoutHandler(event);}, true)
AMenu.addEventListener('focusout', (event) => {focusoutHandler(event);}, true)