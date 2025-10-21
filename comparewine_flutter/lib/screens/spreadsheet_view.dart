import 'dart:io';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../models/wine_price.dart';
import '../services/excel_service.dart';
import '../widgets/wine_data_table.dart';
import '../providers/wine_data_provider.dart';

/// Main spreadsheet view screen for displaying and editing wine price data
class SpreadsheetView extends StatefulWidget {
  const SpreadsheetView({super.key});

  @override
  State<SpreadsheetView> createState() => _SpreadsheetViewState();
}

class _SpreadsheetViewState extends State<SpreadsheetView> {
  final ExcelService _excelService = ExcelService();
  bool _isLoading = false;
  String? _currentFilePath;

  @override
  void initState() {
    super.initState();
    // Load sample data on startup if available
    _loadSampleData();
  }

  Future<void> _loadSampleData() async {
    // This can be used to load default Excel files from assets
    // For now, we'll just start with empty data
  }

  Future<void> _loadExcelFile() async {
    setState(() => _isLoading = true);

    try {
      final file = await _excelService.pickExcelFile();
      if (file != null) {
        final winePrices = await _excelService.readExcelFile(file);

        if (mounted) {
          context.read<WineDataProvider>().setWinePrices(winePrices);
          _currentFilePath = file.path;

          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text('Loaded ${winePrices.length} wine prices from ${file.path.split('/').last}'),
              backgroundColor: Colors.green,
            ),
          );
        }
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Error loading file: $e'),
            backgroundColor: Colors.red,
          ),
        );
      }
    } finally {
      setState(() => _isLoading = false);
    }
  }

  Future<void> _saveToExcel() async {
    if (_currentFilePath == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('No file loaded. Please load a file first.'),
          backgroundColor: Colors.orange,
        ),
      );
      return;
    }

    setState(() => _isLoading = true);

    try {
      final winePrices = context.read<WineDataProvider>().winePrices;
      await _excelService.saveToExcel(winePrices, _currentFilePath!);

      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text('File saved successfully!'),
            backgroundColor: Colors.green,
          ),
        );
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Error saving file: $e'),
            backgroundColor: Colors.red,
          ),
        );
      }
    } finally {
      setState(() => _isLoading = false);
    }
  }

  Future<void> _compareWineries() async {
    final winePrices = context.read<WineDataProvider>().winePrices;
    final wineries = _excelService.groupByWinery(winePrices);

    if (wineries.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('No data to compare. Please load Excel files first.'),
          backgroundColor: Colors.orange,
        ),
      );
      return;
    }

    // Show comparison dialog
    showDialog(
      context: context,
      builder: (context) => _ComparisonDialog(wineries: wineries),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Compare Wine - Price Comparison'),
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
      ),
      body: Column(
        children: [
          // Toolbar
          _buildToolbar(),
          const Divider(height: 1),

          // Spreadsheet area
          Expanded(
            child: _isLoading
                ? const Center(child: CircularProgressIndicator())
                : const WineDataTable(),
          ),

          // Status bar
          _buildStatusBar(),
        ],
      ),
    );
  }

  Widget _buildToolbar() {
    return Container(
      padding: const EdgeInsets.all(8.0),
      color: Colors.grey[200],
      child: Row(
        children: [
          ElevatedButton.icon(
            onPressed: _isLoading ? null : _loadExcelFile,
            icon: const Icon(Icons.folder_open),
            label: const Text('Load Excel'),
          ),
          const SizedBox(width: 8),
          ElevatedButton.icon(
            onPressed: _isLoading ? null : _saveToExcel,
            icon: const Icon(Icons.save),
            label: const Text('Save'),
            style: ElevatedButton.styleFrom(
              backgroundColor: Colors.green[600],
              foregroundColor: Colors.white,
            ),
          ),
          const SizedBox(width: 8),
          ElevatedButton.icon(
            onPressed: _isLoading ? null : _compareWineries,
            icon: const Icon(Icons.compare_arrows),
            label: const Text('Compare'),
            style: ElevatedButton.styleFrom(
              backgroundColor: Colors.blue[600],
              foregroundColor: Colors.white,
            ),
          ),
          const Spacer(),
          Consumer<WineDataProvider>(
            builder: (context, provider, child) {
              return Text(
                '${provider.winePrices.length} wines',
                style: const TextStyle(fontWeight: FontWeight.bold),
              );
            },
          ),
        ],
      ),
    );
  }

  Widget _buildStatusBar() {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      color: Colors.grey[300],
      child: Row(
        children: [
          const Icon(Icons.info_outline, size: 16),
          const SizedBox(width: 8),
          Expanded(
            child: Text(
              _currentFilePath != null
                  ? 'Current file: ${_currentFilePath!.split('/').last}'
                  : 'No file loaded',
              style: const TextStyle(fontSize: 12),
            ),
          ),
        ],
      ),
    );
  }
}

/// Dialog for comparing wine prices across wineries
class _ComparisonDialog extends StatelessWidget {
  final List wineries;

  const _ComparisonDialog({required this.wineries});

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: const Text('Winery Comparison'),
      content: SizedBox(
        width: double.maxFinite,
        child: ListView.builder(
          shrinkWrap: true,
          itemCount: wineries.length,
          itemBuilder: (context, index) {
            final winery = wineries[index];
            return Card(
              margin: const EdgeInsets.symmetric(vertical: 4),
              child: ListTile(
                title: Text(
                  winery.name,
                  style: const TextStyle(fontWeight: FontWeight.bold),
                ),
                subtitle: Text(
                  'Wines: ${winery.wineCount} | Avg Price: €${winery.averagePrice.toStringAsFixed(2)}',
                ),
                trailing: const Icon(Icons.chevron_right),
                onTap: () {
                  // Could show detailed view of this winery
                },
              ),
            );
          },
        ),
      ),
      actions: [
        TextButton(
          onPressed: () => Navigator.of(context).pop(),
          child: const Text('Close'),
        ),
      ],
    );
  }
}
