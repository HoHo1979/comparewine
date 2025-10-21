import 'wine_price.dart';

/// Represents a winery with a collection of wine prices
class Winery {
  String name;
  List<WinePrice> winePrices;

  Winery({
    required this.name,
    List<WinePrice>? winePrices,
  }) : winePrices = winePrices ?? [];

  /// Add a wine price to this winery
  void addWinePrice(WinePrice winePrice) {
    winePrices.add(winePrice);
  }

  /// Get wine prices sorted by name
  List<WinePrice> getSortedByName() {
    final sorted = List<WinePrice>.from(winePrices);
    sorted.sort((a, b) => a.wineName.compareTo(b.wineName));
    return sorted;
  }

  /// Get wine prices sorted by price
  List<WinePrice> getSortedByPrice() {
    final sorted = List<WinePrice>.from(winePrices);
    sorted.sort((a, b) => a.price.compareTo(b.price));
    return sorted;
  }

  /// Get total number of wines
  int get wineCount => winePrices.length;

  /// Get average price
  double get averagePrice {
    if (winePrices.isEmpty) return 0.0;
    final total = winePrices.fold<double>(0.0, (sum, wine) => sum + wine.price);
    return total / winePrices.length;
  }

  @override
  String toString() {
    return 'Winery(name: $name, wines: ${winePrices.length})';
  }
}
