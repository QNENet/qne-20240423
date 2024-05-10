import { unsafeCSS, registerStyles } from '@vaadin/vaadin-themable-mixin/register-styles';

import vaadinTextFieldCss from 'themes/qneTheme/components/vaadin-text-field.css?inline';


if (!document['_vaadintheme_qneTheme_componentCss']) {
  registerStyles(
        'vaadin-text-field',
        unsafeCSS(vaadinTextFieldCss.toString())
      );
      
  document['_vaadintheme_qneTheme_componentCss'] = true;
}

if (import.meta.hot) {
  import.meta.hot.accept((module) => {
    window.location.reload();
  });
}

