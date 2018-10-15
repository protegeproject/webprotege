function downloadSvg(element, filename) {

    // First convert the image to a data URI
    const svg = element.outerHTML;
    const dataIri = "data:image/svg+xml;base64," + window.btoa(svg);

    // Now create an anchor element and set it to download the image
    let a = document.createElement("a");
    a.setAttribute("href", dataIri);
    a.setAttribute("href-lang", "image/svg+xml");
    a.setAttribute("target", "_blank");
    a.setAttribute("download", filename);
    a.click();
}