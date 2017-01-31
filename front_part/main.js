import {render} from 'react-dom';
import {ContentBox} from './components/contentBox';

render(<ContentBox interval={1000} dataLength={200}/>, document.getElementsByClassName('content')[0]);