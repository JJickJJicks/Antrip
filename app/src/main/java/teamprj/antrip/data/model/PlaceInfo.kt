package teamprj.antrip.data.model

class PlaceInfo {
    var num: Int
    var counry: String
    var lat: Double
    var lon:Double
    var name: String
    var accommoodation: String


    constructor() {
        this.num = 0
        this.counry = ""
        this.lat = 0.0
        this.lon = 0.0
        this.name = ""
        this.accommoodation = ""
    }

    constructor(num: Int, counry: String, lat: Double, lon: Double, name: String, accommoodation: String) {
        this.num = num
        this.counry = counry
        this.lat = lat
        this.lon = lon
        this.name = name
        this.accommoodation = accommoodation
    }
}