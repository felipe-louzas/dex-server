import React from 'react';
import { render } from 'react-dom';
import { IntlProvider, addLocaleData } from 'react-intl';

import RootView from './views/RootView';
import registerServiceWorker from './utils/registerServiceWorker';

import pt from 'react-intl/locale-data/pt'

import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/css/bootstrap-theme.css';
import './css/base.css'

addLocaleData(pt);

render(
  <IntlProvider locale={navigator.language}>
    <RootView />
  </IntlProvider>,
  document.getElementById('root')
);

registerServiceWorker();
