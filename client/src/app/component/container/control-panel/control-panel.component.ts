import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { QrLoaderDialogComponent } from 'src/app/component/container/qr-loader-dialog/qr-loader-dialog.component';

@Component({
  selector: 'app-control-panel',
  templateUrl: './control-panel.component.html',
  styleUrls: ['./control-panel.component.css'],
})
export class ControlPanelComponent implements OnInit {
  constructor(private matDialog: MatDialog) {}

  ngOnInit(): void {}

  launchCamera(): void {
    this.matDialog.open(QrLoaderDialogComponent);
  }
}
