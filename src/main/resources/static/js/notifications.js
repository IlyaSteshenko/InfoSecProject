export default class Notifications {

    static notifyUser(string) {
        function fade(notification) {
            const FADE_TIME = 500;
            notification.classList.add("notificationHidden");
            setTimeout(() => {
                notification.remove();
            }, FADE_TIME);
        }

        const EXPIRY_TIME = 5000;
        let container = document.getElementsByClassName("notificationList")[0];
        if (container === undefined) {

            container = document.createElement("div");
            container.classList.add("notificationList");
            document.body.prepend(container);
        }
        let notification = document.createElement("div");
        notification.classList.add("notificationHidden", "notification");
        notification.textContent = string;
        notification.style.color = 'black'
        container.prepend(notification);
        setTimeout(() => notification.classList.remove("notificationHidden"), 10);
        setTimeout(fade, EXPIRY_TIME, notification);
        notification.addEventListener("click", () => {
            fade(notification)
        })
    }
}