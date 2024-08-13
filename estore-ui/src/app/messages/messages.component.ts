import { Component } from '@angular/core';
import { MessageService } from '../message.service';
/**
 * Creates the messages component
 * --No helper functions are needed for this as no operations are being performed on the messages themselves
 */
@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.css']
})
export class MessagesComponent {
  constructor(public messageService: MessageService) {}
}
