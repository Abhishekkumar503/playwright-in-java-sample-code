package com.serenitydojo.playwright;

public class MockingSearchResponse {
    public static final String RESPONSE_WITH_A_SINGLE_ENTRY = """
            {
                             "current_page": 1,
                             "data": [
                                 {
                                     "id": "01KFY1PSQ2N3BWEP64Z8N7ZJ25",
                                     "name": "Thor Hammer",
                                     "description": "Donec malesuada tempus purus. Integer sit amet arcu magna. Sed vel laoreet ligula, non sollicitudin ex. Mauris euismod ac dolor venenatis lobortis. Aliquam iaculis at diam nec accumsan. Ut sodales sed elit et imperdiet. Maecenas vitae molestie mauris. Integer quis placerat libero, in finibus diam. Interdum et malesuada fames ac ante ipsum primis in faucibus.",
                                     "price": 11.14,
                                     "is_location_offer": false,
                                     "is_rental": false,
                                     "co2_rating": "D",
                                     "in_stock": true,
                                     "is_eco_friendly": false,
                                     "product_image": {
                                         "id": "01KFY1PSNW3FGBJW1B85T75WPY",
                                         "by_name": "ANIRUDH",
                                         "by_url": "https://unsplash.com/@lanirudhreddy",
                                         "source_name": "Unsplash",
                                         "source_url": "https://unsplash.com/photos/3esjG-nlgyk",
                                         "file_name": "hammer04.avif",
                                         "title": "Hammer"
                                     },
                                     "category": {
                                         "id": "01KFY1PSNG3C351V80ERYACPDA",
                                         "name": "Hammer"
                                     },
                                     "brand": {
                                         "id": "01KFY1PS8T4SS1BNC7FGEWFR8B",
                                         "name": "ForgeFlex Tools"
                                     }
                                 }
                             ],
                             "from": 1,
                             "last_page": 1,
                             "per_page": 9,
                             "to": 1,
                             "total": 1
                         }
            """;
    public static final String RESPONSE_WITH_NO_ENTRIES = """
            {
                "current_page": 1,
                "data": [],
                "from": 1,
                "last_page": 1,
                "per_page": 9,
                "to": 1,
                "total": 0
            }
            """;
}
