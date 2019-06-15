let peopleData = [{
        "score": 0.1,
        "sentences": [{
            "score": 0.7,
            "magnitude": 0.7,
            "content": "Te amo"
        }, {
            "score": 0.1,
            "magnitude": 0.1,
            "content": "Quiero mucho a mi mama"
        }, {
            "score": -0.4,
            "magnitude": 0.4,
            "content": "Maté a un pájaro ayer"
        }, {
            "score": -0.1,
            "magnitude": 0.1,
            "content": "Ojala no tener hijos"
        }, {
            "score": -0.7,
            "magnitude": 0.7,
            "content": "Odio ir a misa"
        }],
        "name": "Raul",
        "magnitude": 2.2,
        "lang": "es"
    },
    {
        "score": -0.8,
        "sentences": [{
            "score": -0.1,
            "magnitude": 0.1,
            "content": "Esto es un mensaje sin sentimiento."
        }, {
            "score": -0.5,
            "magnitude": 0.5,
            "content": "Odio tener que ir a misa."
        }, {
            "score": 0.7,
            "magnitude": 0.7,
            "content": "Te amo."
        }, {
            "score": 0.5,
            "magnitude": 0.5,
            "content": "Que bonito día."
        }, {
            "score": 0.1,
            "magnitude": 0.1,
            "content": "Quiero mucho a mi mama"
        }],
        "name": "Alex",
        "magnitude": 0.2,
        "lang": "es"
    }
]

colors = ["#3366cc", "#e36141", "#40ab46", "#ffad33", "purple", "orange", "brown", "black"]
peopleData = peopleData.map((p, i) => {
    p.color = colors[i]
    return p
})