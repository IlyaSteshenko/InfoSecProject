export default class Patent {
    constructor(name, id, author, date, description) {
        this.name = name;
        this.id = id;
        this.author = author;
        this.date = date;
        this.description = description;
    }
    name;
    id;
    author;
    date;
    description;
    /**
     *
     * @param onClickFunction
     * функция, которая будет вызвана при нажатии на кнопку
     * принимает 2 параметра
     * 1. HTMLDivElement который бы создан
     * 2. ID патента
     *
     * @returns {HTMLDivElement}
     */
    generateElement(onClickFunction) {
        let element = document.createElement("div");
        element.classList.add("patent");
        element.id = this.id;
        element.innerHTML = `
        <div class="patentHeader">
            <span class="patentName">${this.name}</span>
            <span class="patentId">#${this.id}</span>
        </div>
        <div class="patentBody">
            <div class="patentAuthor">${this.author}</div>
            <div class="patentDate">${this.date}</div>
            <div class="patentDescription">${this.description}</div>
        </div>`;
        if (onClickFunction != null) {
            element.addEventListener("click", onClickFunction(element, this.id));
        }
        return element;

    }
}
