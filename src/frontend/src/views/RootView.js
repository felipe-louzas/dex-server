import React from 'react';
import { Grid, Row, Col, Alert } from 'react-bootstrap'
import axios from 'axios'
import ItemPanel from '../components/ItemPanel'
import SalePanel from '../components/SalePanel'

import './RootView.module.css'

class RootView extends React.Component {

  constructor(props) {
    super(props);
    this.state = {venda: null, loading: false, error: null, catalogo: null}

    if (process.env.NODE_ENV === "production"){
      this.axClient = axios.create({
        baseURL: '/svc',
        timeout: 1000,
      });
    }else{
      this.axClient = axios.create({
        baseURL: 'http://localhost:8080/svc',
        timeout: 1000,
      });
    }
  }

  render() {
    return (
      <Grid fluid={true} className="rootView">
        <ErrorDialog error={this.state.error} onDismiss={this.onErrorDismiss} />
        <VendaNumber venda={this.state.venda} />
        <Row>
          <Col xs={9} md={7} lg={5}>
            <SalePanel venda={this.state.venda} loading={this.state.loading} handler={this}/>
          </Col>
          <Col xs={3} md={5} lg={7}>
            <ItemPanel tipos={this.state.catalogo} handler={this}/>
          </Col>
        </Row>
      </Grid>
    )
  }

  /*
  * Carrega itens do catálogo após ser inserido na página
  */
  componentDidMount() {
    this.setState({loading:true});
    this.axClient
    .get('/catalogo')
    .then((response) => this.setState({catalogo: response.data, loading: false}))
    .catch(this.onError)
  }

  createVenda = (afterCreate) => {
    this.axClient
    .post('/vendas')
    .then((response) => {
      this.setState({venda: response.data, loading: false});
      if (afterCreate) afterCreate();
    })
    .catch(this.onError)
  }

  addItem = (item) => {
    if (item.ingredientes){
      this.axClient
      .post(`/vendas/${this.state.venda.id}/lanches`, item)
      .then((response) => this.setState({venda: response.data, loading: false}))
      .catch(this.onError)
    }else{
      this.axClient
      .post(`/vendas/${this.state.venda.id}/ingredientes`, item)
      .then((response) => this.setState({venda: response.data, loading: false}))
      .catch(this.onError)
    }
  }

  updateVenda = (endpoint) => {
    this.setState({loading:true});
    this.axClient
    .post(`/vendas/${this.state.venda.id}${endpoint}`)
    .then((response) => this.setState({venda: response.data, loading: false}))
    .catch(this.onError)
  }

  onNovaVenda = () => () => {
    this.setState({loading:true});
    this.createVenda();
  }

  onCancelVenda = () => () => {
    this.setState({loading:true});
    this.axClient
    .post(`/vendas/${this.state.venda.id}/cancela`)
    .then((response) => this.setState({venda: null, loading: false}))
    .catch(this.onError)
  }

  onVendeItem = (item) => () => {
    this.setState({loading:true});
    if (!this.state.venda){
      this.createVenda(() => this.addItem(item));
    }else{
      this.addItem(item);
    }
  }

  onItemDelete = (itemId) => () => {
    this.updateVenda(`/lanches/${itemId}/cancela`);
  }

  onLinhaDelete = (itemId, linhaId) => () => {
    this.updateVenda(`/lanches/${itemId}/ingredientes/${linhaId}/remove`);
  }

  onLinhaMinusQtd = (itemId, linhaId) => () => {
    this.updateVenda(`/lanches/${itemId}/ingredientes/${linhaId}/subtract`);
  }

  onPagaVenda = () => () => {
    this.setState({venda: null})
  }

  onError = (error) => {
    this.setState({loading: false, error: error});
  }

  onErrorDismiss = () => {
    this.setState({error: null});
  }

};

const ErrorDialog = ({error, onDismiss}) => {
  if (!error) return null;

  return (
    <Alert className='alert-fixed' bsStyle='danger' onDismiss={onDismiss}>
      <h4>Houve um erro na comunicação com o servidor!</h4>
      <p>{error.toString()}</p>
    </Alert>
  )
}

const VendaNumber = ({venda}) => {
  if (venda) {
    return <div className="vendaNumber"><h4>Venda #: <strong>{venda.id}</strong></h4></div>
  }
  return <div className="vendaNumber"></div>;
}

export default RootView;
