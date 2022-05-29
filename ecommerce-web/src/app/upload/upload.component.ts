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
  public errors: string[] = [];
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
    this.errors = [];
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
            console.log('errors: ', data.body.errors);
            if (data.body.errors) {
              var len = data.body.errors.length;
              len = len > 10 ? 10 : len;
              data.body.errors.slice(0, len).forEach((item: any) => {
                var msg =
                  'Line No. ' +
                  (item.lineNo +1) +
                  ' : ' +
                  item.field +
                  ' - ' +
                  item.messages[0];
                console.log(msg);
                this.errors.push(msg);
              });
            }
          }
          this.fileInput.nativeElement.value = '';
          this.message = data.body.message;
          this.fileAttr = 'Choose File';
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
