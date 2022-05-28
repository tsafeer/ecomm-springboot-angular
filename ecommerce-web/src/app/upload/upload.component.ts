import { CloseScrollStrategy } from '@angular/cdk/overlay';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { OrderService } from '../order.service';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.scss'],
})
export class UploadComponent implements OnInit {
  public isProgress = false;
  constructor(private orderService: OrderService, private router: Router) {}

  ngOnInit(): void {}

  @ViewChild('fileInput') fileInput: ElementRef;
  fileAttr: string = 'Choose File';
  public message: string = '';

  onFileSelect(csvFile: any) {
    this.isProgress = true;
    if (csvFile.target.files && csvFile.target.files[0]) {
      this.fileAttr = '';
      Array.from(csvFile.target.files).forEach((file: any) => {
        this.fileAttr += file.name;
      });
      // HTML5 FileReader API
      let reader = new FileReader();
      reader.onload = (e: any) => {
        let image = new Image();
        image.src = e.target.result;
        image.onload = (rs) => {
          let imgBase64Path = e.target.result;
        };
      };
      reader.readAsDataURL(csvFile.target.files[0]);
      // Reset if duplicate image uploaded again
      this.fileInput.nativeElement.value = '';
    } else {
      this.fileAttr = 'Choose File';
    }
  }

  uploadFile(event: any) {
    console.log('inside upload ');
    this.message = '';
    this.isProgress = true;
    if (event.target.files && event.target.files[0]) {
      this.fileAttr = '';
      this.fileAttr = event.target.files[0].name;

      this.orderService.uploadCsv(event.target.files[0]).subscribe(
        (data) => {
          console.log('success', data.body.status);
          if (data.body.status == 0) {
            this.router.navigate(['/order']);
          } else {
          }
          this.message = data.body.message;
          this.isProgress = false;
        },
        (error) => {
          this.message = 'Oops something went wrong!';
          this.isProgress = false;
        }
      );
    } else {
      this.fileAttr = 'Choose File';
    }
  }
}
