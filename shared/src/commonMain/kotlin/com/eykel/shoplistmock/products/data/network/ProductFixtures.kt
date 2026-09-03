package com.eykel.shoplistmock.products.data.network

/**
 * Local stand-in for what a real `/v1/products` backend would hold. [ProductMockEngine.kt]
 * serves these instead of hitting a socket.
 */
internal object ProductFixtures {

    val all: List<ProductDetailDto> = listOf(
        ProductDetailDto(
            id = "p1",
            name = "Arroz Integral 1kg",
            price = 8.90,
            imageUrl = "https://picsum.photos/seed/shoplist-p1/600/600",
            category = "Graos",
            inStock = true,
            description = "Arroz integral tipo 1, graos longos e soltos. Fonte de fibras, " +
                "ideal para o dia a dia.",
            rating = 4.6,
            stockCount = 42
        ),
        ProductDetailDto(
            id = "p2",
            name = "Feijao Preto 1kg",
            price = 7.50,
            imageUrl = "https://picsum.photos/seed/shoplist-p2/600/600",
            category = "Graos",
            inStock = true,
            description = "Feijao preto selecionado, cozimento uniforme e casca fina.",
            rating = 4.4,
            stockCount = 37
        ),
        ProductDetailDto(
            id = "p3",
            name = "Leite Integral 1L",
            price = 5.20,
            imageUrl = "https://picsum.photos/seed/shoplist-p3/600/600",
            category = "Laticinios",
            inStock = true,
            description = "Leite integral UHT, embalagem longa vida.",
            rating = 4.2,
            stockCount = 80
        ),
        ProductDetailDto(
            id = "p4",
            name = "Cafe Torrado e Moido 500g",
            price = 14.90,
            imageUrl = "https://picsum.photos/seed/shoplist-p4/600/600",
            category = "Mercearia",
            inStock = true,
            description = "Blend torra media, notas de chocolate e castanhas.",
            rating = 4.8,
            stockCount = 25
        ),
        ProductDetailDto(
            id = "p5",
            name = "Azeite Extra Virgem 500ml",
            price = 24.90,
            imageUrl = "https://picsum.photos/seed/shoplist-p5/600/600",
            category = "Mercearia",
            inStock = false,
            description = "Acidez maxima de 0,3%. Colheita unica, prensado a frio.",
            rating = 4.7,
            stockCount = 0
        ),
        ProductDetailDto(
            id = "p6",
            name = "Maca Fuji (kg)",
            price = 9.90,
            imageUrl = "https://picsum.photos/seed/shoplist-p6/600/600",
            category = "Hortifruti",
            inStock = true,
            description = "Maca fuji doce e crocante, selecionada a mao.",
            rating = 4.3,
            stockCount = 60
        ),
        ProductDetailDto(
            id = "p7",
            name = "Peito de Frango (kg)",
            price = 18.50,
            imageUrl = "https://picsum.photos/seed/shoplist-p7/600/600",
            category = "Acougue",
            inStock = true,
            description = "Peito de frango resfriado, sem osso.",
            rating = 4.1,
            stockCount = 18
        ),
        ProductDetailDto(
            id = "p8",
            name = "Papel Higienico 12 rolos",
            price = 22.90,
            imageUrl = "https://picsum.photos/seed/shoplist-p8/600/600",
            category = "Limpeza",
            inStock = true,
            description = "Folha dupla, alta absorcao, 30 metros por rolo.",
            rating = 4.5,
            stockCount = 54
        )
    )

    fun summaries(): List<ProductDto> = all.map {
        ProductDto(
            id = it.id,
            name = it.name,
            price = it.price,
            imageUrl = it.imageUrl,
            category = it.category,
            inStock = it.inStock
        )
    }

    fun find(id: String): ProductDetailDto? = all.find { it.id == id }
}
