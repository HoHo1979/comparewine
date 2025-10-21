import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../models/wine_price.dart';
import '../providers/wine_data_provider.dart';

/// Interactive data table widget for displaying and editing wine price data
class WineDataTable extends StatefulWidget {
  const WineDataTable({super.key});

  @override
  State<WineDataTable> createState() => _WineDataTableState();
}

class _WineDataTableState extends State<WineDataTable> {
  int? _sortColumnIndex;
  bool _sortAscending = true;
  final _controllers = <int, Map<String, TextEditingController>>{};

  @override
  void dispose() {
    // Dispose all text controllers
    for (final row in _controllers.values) {
      for (final controller in row.values) {
        controller.dispose();
      }
    }
    super.dispose();
  }

  void _sort<T>(Comparable<T> Function(WinePrice wine) getField, int columnIndex, bool ascending) {
    final provider = context.read<WineDataProvider>();
    provider.sortWinePrices((a, b) {
      final aValue = getField(a);
      final bValue = getField(b);
      return ascending
          ? Comparable.compare(aValue, bValue)
          : Comparable.compare(bValue, aValue);
    });

    setState(() {
      _sortColumnIndex = columnIndex;
      _sortAscending = ascending;
    });
  }

  TextEditingController _getController(int rowIndex, String field, String initialValue) {
    _controllers[rowIndex] ??= {};
    _controllers[rowIndex]![field] ??= TextEditingController(text: initialValue);
    return _controllers[rowIndex]![field]!;
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<WineDataProvider>(
      builder: (context, provider, child) {
        if (provider.winePrices.isEmpty) {
          return Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Icon(Icons.table_chart, size: 64, color: Colors.grey[400]),
                const SizedBox(height: 16),
                Text(
                  'No data loaded',
                  style: TextStyle(fontSize: 18, color: Colors.grey[600]),
                ),
                const SizedBox(height: 8),
                Text(
                  'Click "Load Excel" to import wine price data',
                  style: TextStyle(color: Colors.grey[500]),
                ),
              ],
            ),
          );
        }

        return SingleChildScrollView(
          scrollDirection: Axis.vertical,
          child: SingleChildScrollView(
            scrollDirection: Axis.horizontal,
            child: DataTable(
              sortColumnIndex: _sortColumnIndex,
              sortAscending: _sortAscending,
              columnSpacing: 20,
              headingRowColor: MaterialStateProperty.all(Colors.blue[50]),
              dataRowMinHeight: 48,
              dataRowMaxHeight: 56,
              columns: [
                DataColumn(
                  label: const Text('Wine Name', style: TextStyle(fontWeight: FontWeight.bold)),
                  onSort: (columnIndex, ascending) =>
                      _sort((wine) => wine.wineName, columnIndex, ascending),
                ),
                DataColumn(
                  label: const Text('Vintage', style: TextStyle(fontWeight: FontWeight.bold)),
                  onSort: (columnIndex, ascending) =>
                      _sort((wine) => wine.wineVintage, columnIndex, ascending),
                ),
                DataColumn(
                  label: const Text('Price', style: TextStyle(fontWeight: FontWeight.bold)),
                  numeric: true,
                  onSort: (columnIndex, ascending) =>
                      _sort((wine) => wine.price, columnIndex, ascending),
                ),
                DataColumn(
                  label: const Text('Unit', style: TextStyle(fontWeight: FontWeight.bold)),
                ),
                DataColumn(
                  label: const Text('Bottle Size', style: TextStyle(fontWeight: FontWeight.bold)),
                ),
                DataColumn(
                  label: const Text('Origin', style: TextStyle(fontWeight: FontWeight.bold)),
                  onSort: (columnIndex, ascending) =>
                      _sort((wine) => wine.origin, columnIndex, ascending),
                ),
                DataColumn(
                  label: const Text('Country', style: TextStyle(fontWeight: FontWeight.bold)),
                  onSort: (columnIndex, ascending) =>
                      _sort((wine) => wine.country, columnIndex, ascending),
                ),
                DataColumn(
                  label: const Text('Score', style: TextStyle(fontWeight: FontWeight.bold)),
                  numeric: true,
                  onSort: (columnIndex, ascending) =>
                      _sort((wine) => wine.score, columnIndex, ascending),
                ),
                DataColumn(
                  label: const Text('Supplier', style: TextStyle(fontWeight: FontWeight.bold)),
                  onSort: (columnIndex, ascending) =>
                      _sort((wine) => wine.supplier, columnIndex, ascending),
                ),
                const DataColumn(
                  label: Text('Actions', style: TextStyle(fontWeight: FontWeight.bold)),
                ),
              ],
              rows: List<DataRow>.generate(
                provider.winePrices.length,
                (index) => _buildDataRow(provider.winePrices[index], index, provider),
              ),
            ),
          ),
        );
      },
    );
  }

  DataRow _buildDataRow(WinePrice wine, int index, WineDataProvider provider) {
    return DataRow(
      cells: [
        DataCell(
          SizedBox(
            width: 200,
            child: Text(wine.wineName),
          ),
        ),
        DataCell(
          SizedBox(
            width: 80,
            child: Text(wine.wineVintage),
          ),
        ),
        DataCell(
          SizedBox(
            width: 80,
            child: Text(
              '€${wine.price.toStringAsFixed(2)}',
              style: const TextStyle(fontWeight: FontWeight.w500),
            ),
          ),
        ),
        DataCell(Text(wine.priceUnit)),
        DataCell(Text(wine.bottleSize)),
        DataCell(
          SizedBox(
            width: 120,
            child: Text(wine.origin),
          ),
        ),
        DataCell(Text(wine.country)),
        DataCell(
          Text(
            wine.score > 0 ? wine.score.toStringAsFixed(1) : '-',
          ),
        ),
        DataCell(
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
            decoration: BoxDecoration(
              color: _getSupplierColor(wine.supplier),
              borderRadius: BorderRadius.circular(4),
            ),
            child: Text(
              wine.supplier,
              style: const TextStyle(
                color: Colors.white,
                fontSize: 12,
                fontWeight: FontWeight.bold,
              ),
            ),
          ),
        ),
        DataCell(
          Row(
            children: [
              IconButton(
                icon: const Icon(Icons.edit, size: 18),
                onPressed: () => _editWine(wine, index, provider),
                tooltip: 'Edit',
              ),
              IconButton(
                icon: const Icon(Icons.delete, size: 18, color: Colors.red),
                onPressed: () => _deleteWine(index, provider),
                tooltip: 'Delete',
              ),
            ],
          ),
        ),
      ],
    );
  }

  Color _getSupplierColor(String supplier) {
    switch (supplier.toUpperCase()) {
      case 'DUCLOT':
        return Colors.purple;
      case 'SOVEX':
        return Colors.teal;
      case 'LOUIS':
        return Colors.indigo;
      default:
        return Colors.grey;
    }
  }

  void _editWine(WinePrice wine, int index, WineDataProvider provider) {
    showDialog(
      context: context,
      builder: (context) => _EditWineDialog(
        wine: wine,
        onSave: (updatedWine) {
          provider.updateWinePrice(index, updatedWine);
        },
      ),
    );
  }

  void _deleteWine(int index, WineDataProvider provider) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Delete Wine'),
        content: const Text('Are you sure you want to delete this wine entry?'),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(context).pop(),
            child: const Text('Cancel'),
          ),
          TextButton(
            onPressed: () {
              provider.removeWinePrice(index);
              Navigator.of(context).pop();
            },
            style: TextButton.styleFrom(foregroundColor: Colors.red),
            child: const Text('Delete'),
          ),
        ],
      ),
    );
  }
}

/// Dialog for editing wine price data
class _EditWineDialog extends StatefulWidget {
  final WinePrice wine;
  final Function(WinePrice) onSave;

  const _EditWineDialog({required this.wine, required this.onSave});

  @override
  State<_EditWineDialog> createState() => _EditWineDialogState();
}

class _EditWineDialogState extends State<_EditWineDialog> {
  late TextEditingController _nameController;
  late TextEditingController _vintageController;
  late TextEditingController _priceController;
  late TextEditingController _priceUnitController;
  late TextEditingController _bottleSizeController;
  late TextEditingController _originController;
  late TextEditingController _countryController;
  late TextEditingController _scoreController;

  @override
  void initState() {
    super.initState();
    _nameController = TextEditingController(text: widget.wine.wineName);
    _vintageController = TextEditingController(text: widget.wine.wineVintage);
    _priceController = TextEditingController(text: widget.wine.price.toString());
    _priceUnitController = TextEditingController(text: widget.wine.priceUnit);
    _bottleSizeController = TextEditingController(text: widget.wine.bottleSize);
    _originController = TextEditingController(text: widget.wine.origin);
    _countryController = TextEditingController(text: widget.wine.country);
    _scoreController = TextEditingController(text: widget.wine.score.toString());
  }

  @override
  void dispose() {
    _nameController.dispose();
    _vintageController.dispose();
    _priceController.dispose();
    _priceUnitController.dispose();
    _bottleSizeController.dispose();
    _originController.dispose();
    _countryController.dispose();
    _scoreController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: const Text('Edit Wine'),
      content: SingleChildScrollView(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            TextField(
              controller: _nameController,
              decoration: const InputDecoration(labelText: 'Wine Name'),
            ),
            TextField(
              controller: _vintageController,
              decoration: const InputDecoration(labelText: 'Vintage'),
            ),
            TextField(
              controller: _priceController,
              decoration: const InputDecoration(labelText: 'Price'),
              keyboardType: TextInputType.number,
            ),
            TextField(
              controller: _priceUnitController,
              decoration: const InputDecoration(labelText: 'Price Unit'),
            ),
            TextField(
              controller: _bottleSizeController,
              decoration: const InputDecoration(labelText: 'Bottle Size'),
            ),
            TextField(
              controller: _originController,
              decoration: const InputDecoration(labelText: 'Origin'),
            ),
            TextField(
              controller: _countryController,
              decoration: const InputDecoration(labelText: 'Country'),
            ),
            TextField(
              controller: _scoreController,
              decoration: const InputDecoration(labelText: 'Score'),
              keyboardType: TextInputType.number,
            ),
          ],
        ),
      ),
      actions: [
        TextButton(
          onPressed: () => Navigator.of(context).pop(),
          child: const Text('Cancel'),
        ),
        ElevatedButton(
          onPressed: () {
            final updatedWine = widget.wine.copyWith(
              wineName: _nameController.text,
              wineVintage: _vintageController.text,
              price: double.tryParse(_priceController.text) ?? widget.wine.price,
              priceUnit: _priceUnitController.text,
              bottleSize: _bottleSizeController.text,
              origin: _originController.text,
              country: _countryController.text,
              score: double.tryParse(_scoreController.text) ?? widget.wine.score,
            );
            widget.onSave(updatedWine);
            Navigator.of(context).pop();
          },
          child: const Text('Save'),
        ),
      ],
    );
  }
}
