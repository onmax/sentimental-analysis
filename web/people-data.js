let peopleData = []
colors = ["#3366cc", "#e36141", "#40ab46", "#ffad33", "purple", "orange", "brown", "black"]
const request = async () => {
    peopleData = await fetch('http://localhost:8080/data')
        .then(response => response = response.json())
        .then(json => json.map((p, i) => {
            p.color = colors[i]
            return p
        }));
}
request()
console.log(peopleData)
peopleData = peopleData.map((p, i) => {
    p.color = colors[i]
    return p
})