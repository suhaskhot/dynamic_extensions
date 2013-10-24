var DesignModeBizLogic = {

	controlsLayoutMatrix : new Array(),
	maxSequenceNumber : 0,
	maxXPos : 0,

	loadMatrixWithControls : function(controlsMap, controlsOrder) {
		this.controlsLayoutMatrix = new Array();
		for ( var key = 0; key < controlsOrder.length; key++) {

			var control = controlsMap[controlsOrder[key]];
			if (control != undefined && control != null) {
				var sequenceNum = parseInt(control.get('sequenceNumber'));
				var xPos = parseInt(control.get('xPos'));

				// create a matrix by inserting an array if it has not been
				// added
				// already

				if (this.controlsLayoutMatrix[sequenceNum] == undefined
						|| this.controlsLayoutMatrix[sequenceNum] == null) {
					this.controlsLayoutMatrix[sequenceNum] = new Array();
				}

				// push the control name into the matrix
				this.controlsLayoutMatrix[sequenceNum].push(control
						.get('controlName'));

			}
		}

	},

	populateLayoutGrid : function() {

		var currentColumnCount = 0;
		var gridRowNum = 0;

		var gridObject = Main.designModeViewPointer.getGridObject();

		gridObject.clearAll();

		for ( var rowCounter = 0; rowCounter < this.controlsLayoutMatrix.length; rowCounter++) {
			var matrixRow = this.controlsLayoutMatrix[rowCounter];
			var gridColumnNum = 0;
			if (matrixRow != undefined && matrixRow != null) {

				// add row
				gridObject.addRow(gridObject.uid(), "");

				// add columns
				for ( var columnCounter = 0; columnCounter < matrixRow.length; columnCounter++) {

					var controlName = matrixRow[columnCounter];

					if (controlName != undefined) {
						// add column

						if (columnCounter > currentColumnCount) {
							gridObject.insertColumn(columnCounter, '', 'ro',
									'300', '', 'center');

							currentColumnCount++

						}
						// put control in grid

						gridObject.cellByIndex(gridRowNum, gridColumnNum)
								.setValue(controlName);
						gridColumnNum++;
					}
				}
				gridRowNum++;
			}
		}

	},

	reArrangeRows : function(start, end, bufferValue, columnNum, up) {
		var gridObject = Main.designModeViewPointer.getGridObject();
		var buffer = bufferValue;
		var buffer1 = "";
		if (up) {
			for ( var cntr = start; cntr >= end; cntr--) {

				buffer1 = gridObject.cellByIndex(cntr, columnNum).getValue();
				gridObject.cellByIndex(cntr, columnNum).setValue(buffer);
				buffer = buffer1;
			}
		} else {
			for ( var cntr = start; cntr <= end; cntr++) {

				buffer1 = gridObject.cellByIndex(cntr, columnNum).getValue();
				gridObject.cellByIndex(cntr, columnNum).setValue(buffer);
				buffer = buffer1;
			}
		}
	},

	// write code to parse grid

	populateControlPositions : function() {
		var gridObject = Main.designModeViewPointer.getGridObject();
		var numberOfRows = gridObject.getRowsNum();
		var numberOfColumns = gridObject.getColumnsNum();

		for ( var rowCounter = 0; rowCounter < numberOfRows; rowCounter++) {

			for ( var columnCounter = 0; columnCounter < numberOfColumns; columnCounter++) {

				var controlName = gridObject.cellByIndex(rowCounter,
						columnCounter).getValue();
				var control = Main.formView.getFormModel().get(
						'controlObjectCollection')[controlName];

				if (control != undefined) {
					var _seq = rowCounter + 1;
					var _xPos = columnCounter + 1;
					Main.formView.getFormModel().get('controlObjectCollection')[controlName]
							.set({
								sequenceNumber : _seq,
								xPos : _xPos
							});
				}
			}
		}
	}

}