import { ThemableMixin } from '@vaadin/vaadin-themable-mixin/vaadin-themable-mixin.js';
import { customElement, property} from 'lit/decorators.js';
import { css, html, LitElement } from 'lit';
import '@vaadin/icon';

@customElement('web-cam')
export class WebCam extends ThemableMixin(LitElement) {
  @property()
  image = '';
  @property()
  buttons = 'none';
  @property()
  width = 320;
  @property()
  height = 240;

  cameraStream : MediaStream | null = null;
  stream  : HTMLVideoElement | null = null;
  snapshot : HTMLElement | null = null;
  capture : HTMLCanvasElement | null = null;

  connectedCallback() {
	super.connectedCallback();
	const shadow = this.shadowRoot;
	if (shadow) {
      this.stream  = <HTMLVideoElement | null>shadow.getElementById( "stream" );
      this.snapshot = shadow.getElementById( "snapshot" );
      this.capture = <HTMLCanvasElement | null>shadow.getElementById( "capture" );
      this.startStreaming();
    }
  }

  disconnectedCallback() {
	super.disconnectedCallback();
	this.stopStreaming();
  }

  static get is() {
	  return "web-cam";
  }

  static get styles() {
    return css`
		:host .container {
			display: inline-block;
		}
		:host .button-group {
			display: none;
			flex-direction: row;
			justify-content: space-between;
		}
    `;
  }

  getStream() : HTMLElement | null {
	return this.stream;
  }

  setStreamAndPlay(mediaStream : any) : void {
     this.cameraStream = mediaStream;
     if (this.stream) this.stream.srcObject = mediaStream;
	 if (this.stream) this.stream.play();	
  }

  startStreaming() {
	if (this.stream && this.snapshot) {
		this.stream.style.display = "inherit";
		this.snapshot.style.display = "none";
	}
	const mediaSupport = 'mediaDevices' in navigator;
	if( mediaSupport && null == this.cameraStream ) {
		navigator.mediaDevices.getUserMedia( { video: true } )
		.then( mediaStream => this.setStreamAndPlay(mediaStream) )
		.catch( function( err ) {
			console.log( "Unable to access camera: " + err );
		});
	} else {
		console.log('Your browser does not support media devices.');
		return;
	}
  }

  stopStreaming() {
	const cameraStream = this.cameraStream;
	if( cameraStream ) {
		var track = cameraStream.getTracks()[ 0 ];
		track.stop();
        if (this.stream) this.stream.load();
		this.cameraStream = null;
	}
  }

  captureSnapshot() {
	if( null != this.cameraStream ) {
      const capture = this.capture;
      const stream = this.stream;
      if (capture && stream) {
  	    var ctx = capture.getContext( '2d' );
	    var img = new Image();

        ctx?.drawImage( stream, 0, 0, capture.width, capture.height );
        const imageData = capture.toDataURL( "image/png" );
        this.handleChange(imageData);
        img.src = imageData;
        img.width = stream.width;

        if (this.snapshot) {
          this.snapshot.style.display = "block";
          stream.style.display = "none";
          this.snapshot.innerHTML = '';
          this.snapshot.appendChild( img );
          this.stopStreaming();
        }
      }
    }
  }

  handleChange(value: string) {
	this.image = value;
	const event = new CustomEvent('image-changed', {
		detail: value,
        composed: true,
        cancelable: true,
        bubbles: true		
	});
	this.dispatchEvent(event);
  }

  render() {
    return html`
      <div id="container" class="container">
        <div id="webcam" class="webcam-area">
          <video id="stream" @click=${this.captureSnapshot} width=${this.width} height=${this.height}></video>
          <canvas style="display: none" id="capture" width=${this.width} height=${this.height}></canvas>
          <div style="display: none" @click=${this.startStreaming} id="snapshot"></div>
        </div>
        <div style="display: ${this.buttons}" class="button-group">
          <button id="btn-start" type="button" @click=${this.startStreaming} class="button"><vaadin-icon icon="vaadin:play-circle"></vaadin-icon></button>
          <button id="btn-stop" type="button" @click=${this.stopStreaming} class="button"><vaadin-icon icon="vaadin:stop"></vaadin-icon></button>
          <button id="btn-capture" type="button" @click=${this.captureSnapshot} class="button"><vaadin-icon icon="vaadin:camera"></vaadin-icon></button>
        </div>
      </div>
    `;
  }

}
