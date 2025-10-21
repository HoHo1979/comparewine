import 'package:flutter/foundation.dart';
import '../models/wine_price.dart';

/// Provider for managing wine price data state
class WineDataProvider with ChangeNotifier {
  List<WinePrice> _winePrices = [];

  List<WinePrice> get winePrices => _winePrices;

  /// Set the complete list of wine prices
  void setWinePrices(List<WinePrice> winePrices) {
    _winePrices = winePrices;
    notifyListeners();
  }

  /// Add a new wine price
  void addWinePrice(WinePrice winePrice) {
    _winePrices.add(winePrice);
    notifyListeners();
  }

  /// Update a wine price at a specific index
  void updateWinePrice(int index, WinePrice winePrice) {
    if (index >= 0 && index < _winePrices.length) {
      _winePrices[index] = winePrice;
      notifyListeners();
    }
  }

  /// Remove a wine price at a specific index
  void removeWinePrice(int index) {
    if (index >= 0 && index < _winePrices.length) {
      _winePrices.removeAt(index);
      notifyListeners();
    }
  }

  /// Sort wine prices using a custom comparator
  void sortWinePrices(int Function(WinePrice a, WinePrice b) compare) {
    _winePrices.sort(compare);
    notifyListeners();
  }

  /// Clear all wine prices
  void clear() {
    _winePrices.clear();
    notifyListeners();
  }

  /// Filter wine prices by supplier
  List<WinePrice> filterBySupplier(String supplier) {
    return _winePrices.where((wine) => wine.supplier == supplier).toList();
  }

  /// Get unique suppliers
  List<String> get uniqueSuppliers {
    final suppliers = <String>{};
    for (final wine in _winePrices) {
      if (wine.supplier.isNotEmpty) {
        suppliers.add(wine.supplier);
      }
    }
    return suppliers.toList()..sort();
  }

  /// Get wine prices grouped by wine name for comparison
  Map<String, List<WinePrice>> groupByWineName() {
    final grouped = <String, List<WinePrice>>{};
    for (final wine in _winePrices) {
      final key = wine.wineName.toLowerCase();
      grouped[key] ??= [];
      grouped[key]!.add(wine);
    }
    return grouped;
  }

  /// Find best price for each wine across suppliers
  List<WinePrice> getBestPrices() {
    final grouped = groupByWineName();
    final bestPrices = <WinePrice>[];

    for (final wines in grouped.values) {
      if (wines.isNotEmpty) {
        wines.sort((a, b) => a.price.compareTo(b.price));
        bestPrices.add(wines.first);
      }
    }

    return bestPrices;
  }
}
