const typedText = document.querySelector(".typed-text");
const cursor = document.querySelector(".cursor");

const textArray = ["get the latest news", "analyze stock financials", "track your portfolio"];
const typingDelay = 200;
const erasingDelay = 100;
const newTextDelay = 2000;
let textArrayIndex = 0;
let charIndex = 0;

function type() {
    // if letter index is less then length of word
    if (charIndex < textArray[textArrayIndex].length) {
        cursor.classList.add("typing");
        // add the letter to the content
        typedText.textContent += textArray[textArrayIndex].charAt(charIndex);
        charIndex++;

        // call the type function after a delay
        setTimeout(type, typingDelay);
    } 
    else {
        // finished typing the word
        cursor.classList.remove("typing");

        //call the erase function after a delay
        setTimeout(erase, newTextDelay);
    }
}

function erase() {
    // if index is more than 0, means word has not been completely deleted
    if (charIndex > 0) {
        cursor.classList.add("typing");
        // replace the text content with substring of word to simulate deleting of word;
        typedText.textContent = textArray[textArrayIndex].substring(0, charIndex - 1);
        charIndex--;

        // call erase function after a delay
        setTimeout(erase, erasingDelay);
    } 
    else {
        // word has been completely deleted
        cursor.classList.remove("typing");

        // move on to the next word
        textArrayIndex++;

        // set index to 0 to restart if there is no more word after
        if (textArrayIndex >= textArray.length){
            textArrayIndex = 0;
        }

        // call type function after delay
        setTimeout(type, typingDelay + 1100);
    }
}

document.addEventListener("DOMContentLoaded", function() {
  if (textArray.length > 0){
    setTimeout(type, newTextDelay + 250);
  }
});